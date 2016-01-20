![COEL rocks!](/source/Web/web-app/images/coel_gradient-01.png "COEL rocks!")
Cloud Chemistry Simulation Framework

The framework is accessible at [coel-sim.org](http://coel-sim.org).

---

# Intro

COEL is a web-based chemistry simulation framework.

COEL's most prominent features include ODE-based simulations of chemical reaction networks and multicompartment reaction networks, with rich options for user interactions with those networks. COEL provides DNA-strand displacement transformations and visualization (and is to our knowledge the first CRN framework to do so), GA optimization of rate constants, expression validation, an application-wide plotting engine, and SBML/Octave/Matlab export. COEL's visually pleasing and intuitive user interface, simulations that run on a large computational grid, reliable database storage, and transactional services make it ideal for collaborative research and education.
Besides chemical reaction networks, COEL provides a unified and extendible environment for the definition and manipulation of complex Boolean and real-valued networks.

COEL framework is built on Grails, Spring, Hibernate, and GridGain technology stack. 

A paper summarizing COEL's functionality and architecture can be found [here](http://arxiv.org/abs/1407.4027).

This project has been developed and maintained by [Peter Banda](http://peterbanda.net).</br>
If you wish to collaborate in the development of this project contact me at `banda@pdx.edu`.

---

# Deployment Guide

Here we provide a step-by-step guide showing how COEL can be deployed locally to Linux-based OS such as Ubuntu and partially to Mac OS (not tested). For Windows you can still use this guide but you need to adjust the os-specific parts such as settings of environmental variables. This guide applies to the most recent COEL release - 0.8.3.

#### 1. Install Java 1.7

* Install OpenJDK or Oracle JDK version 1.7 manually or by using the package manager:
```
sudo apt-get install openjdk-7-jdk
```
or
```
sudo add-apt-repository ppa:webupd8team/java
sudo apt-get update
sudo apt-get install oracle-java7-installer
```
* Be sure that your `JAVA_HOME` points to the installation! For instance:
```
JAVA_HOME=/usr/lib/jvm/java-7-openjdk-amd64
```

#### 2. Set up the environmental variables

* Download the [file](http://peterbanda.net/coel/0.8.3/environmental_setting) or copy paste the following:
```
export COEL_DBUSER=coeladmin
export COEL_DBPASSWORD=CHANGE_ME
export COEL_DB=coel
export COEL_DBHOST=localhost
export COEL_DBPORT=5432

export COEL_GRIDMASTER=localhost
export COEL_GRID_METRICS_TEMPFOLDER=
export COEL_GRID_FS_DISCOVERY_ENABLED=true
export COEL_GRID_STEALING_ENABLED=true
export COEL_AC_DYNAMICS_ANALYSIS_PARALLEL_TASKS=20

export COEL_EMAIL_HOST=CHANGE_ME
export COEL_EMAIL_PORT=465
export COEL_EMAIL_USERNAME=CHANGE_ME
export COEL_EMAIL_PASSWORD=CHANGE_ME

# uncomment and adapt the path if you installed gridgain
# export GRIDGAIN_HOME=/opt/gridgain-fabric-os-6.5.0
```
* Override the variable values, especially those marked with `CHANGE_ME`.

* Copy the settings to the central init script such as `/etc/environment` (recommended), `.bashrc`, or `.bash_profile`.
  Note that to load these variables a new login or restart might be required.

#### 3. Set up database

* Download and install PostgreSQL db server from [here](http://www.postgresql.org/download/) or using Ubuntu package manager as:</br>
`sudo apt-get install postgresql-9.4`

 Note that we use the version 9.3 but any 9.x version should work.
 
* Download a zip containing COEL db init scripts and dump from [here](http://peterbanda.net/coel/0.8.3/db_init_scripts.zip) and unpack it.

* Open `create_db_and_user.sql` and set the password of the COEL db user. This must match the environmental settings from the step 2. Run `create_db_and_user.sh`.

* Run `restore_init_dump.sh`.
 
#### 4. Set up application server (Tomcat)
There are two options how to prepare Tomcat's app server hosting COEL app:

* All-in-one:
  * Download a preconfigured Tomcat **including** the COEL prebuilt war from [here](https://peterbanda.net/coel/0.8.3/apache-tomcat-7.0.56_with_coel_0.8.3.zip).

* Tomcat and war separately:
  * Download Tomcat 7.x.x version from [here](https://tomcat.apache.org/download-70.cgi).

  * Download a prebuilt COEL app war from [here](https://peterbanda.net/coel/0.8.3/coel-web-0.8.3.war).

  * Move/copy the war to `apache-tomcat-7.x.x/webapps` and rename it to `ROOT.war`.

#### 5. Set up GridGain computational fabric (optional)

The COEL app already comes with the GridGain lib and the technology is well integrated, however, if you want to spread the grid over several other nodes it is required to configure a standalone GridGain instance per each node.

* Download a preconfigured GridGain app and unzip. Note that we use the version 6.5.0 (community edition), which is freely available.
* Uncomment the GridGain environmental setting (step 2) and adjust the path accordingly
```
# uncomment and adapt the path if you installed gridgain
# export GRIDGAIN_HOME=/opt/gridgain-fabric-os-6.5.0
```
* Once the server hosting the master node is up (step 6) you can launch a new computational node by running
```
/gridgain-fabric-os-6.5.0/bin/start.sh
```
#### 6. Launch the application

* Go to the Tomcat's bin folder such as `apache-tomcat-7.0.56/bin` and launch `startup.sh`. To stop the app run `shutdown.sh`.
