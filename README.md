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
If you wish to collaborate in the develepment of this project contact me at `banda@pdx.edu`.

---

# Deployment Guide

Here we provide a step-by-step guide showing how COEL can be deployed locally to Linux-based OS such as Ubuntu and partially to Mac OS (not tested). For Windows you can still use this guide but you need to adjust os-specific parts such as settings of environmental variables.

#### 1. Set up the evironmental variables

* Download the file or copy paste the following:
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
* Copy the settings to the central init script such as `/etc/environment` (recommended), `.bashrc`, and `.bash_profile`.
  Note that to load these variables, a new login or restart might be required.

#### 2. Set up COEL database

* Download PostgreSQL db server from [here](http://www.postgresql.org/download/) or install it using Ubuntu package manager as `sudo apt-get install postgresql-9.4`. We use the version 9.3 but any 9.x version should work.
* Download a zip containing COEL db init scripts and dump from [here](http://peterbanda.net) and unpack it.
* Open `create_db_and_user.sql` and set the password of the COEL db user. This must match the environemntal settings from the step 1. Run `create_db_and_user.sh`.
* Run `restore_init_dump.sh`.
 
#### 3. Set up COEL application server (Tomcat)
