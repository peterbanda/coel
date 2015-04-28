package edu.banda.coel.server.common;

import java.io.Serializable;
import java.util.Properties;

import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.dialect.Dialect;
import org.hibernate.engine.SessionImplementor;
import org.hibernate.id.Configurable;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.id.factory.DefaultIdentifierGeneratorFactory;
import org.hibernate.id.factory.IdentifierGeneratorFactory;
import org.hibernate.persister.entity.EntityPersister;
import org.hibernate.type.Type;

public class ConfigurableAssignedGenerator implements IdentifierGenerator, Configurable {
   
   /**
    * The delegate parameter specifies the underlying ID generator to use.
    */
   public static final String DELEGATE = "delegate";
   
   private IdentifierGenerator assignedGenerator;
   private IdentifierGenerator delegateGenerator;
   private String entityName;
   
   public Serializable generate(SessionImplementor session, Object object) throws HibernateException {
     EntityPersister persister = session.getEntityPersister(entityName, object);
     Serializable id = persister.getIdentifier(object, session);
      if (id != null) {
         return assignedGenerator.generate(session, object);
      }
      return delegateGenerator.generate(session, object);
   }

   public void configure(Type type, Properties params, Dialect d) throws MappingException {
      String generatorName = params.getProperty(DELEGATE);
      if (generatorName == null) {
        throw new MappingException("param named \"delegate\" is required for object generation strategy");
      }
      entityName = params.getProperty(ENTITY_NAME);
      if (entityName == null) {
        throw new MappingException("no entity name");
      }

      IdentifierGeneratorFactory factory = new DefaultIdentifierGeneratorFactory();
      factory.setDialect(d);
      delegateGenerator = factory.createIdentifierGenerator(generatorName, type, params);
      assignedGenerator = factory.createIdentifierGenerator("assigned", type, params);
   }
}