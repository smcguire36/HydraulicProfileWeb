# HydraulicProfileWeb
Web-Based Visual Hydraulic Profiles
This repository is a Java based application that is built using Java Server Faces (JSF) 2.3 along with the PrimeFaces and ICEFaces libraries.  

The original Hydraulic Profile application was a C/C++ application written as a DOS command line application that took text input files and produced text based output files.
Then a Visual Basic 6 wrapper application was created to provide a database and Windows UI front end to the basic model application.

This application will provide the following features:

* Modern UI using JSF and ICEFaces
* Graphical flow chart like UI using PrimeFaces flowchart component
* Azure DB database backend

The application will run on the Payara 5 application server (which is the new open source version of GlassFish).  Ultimately I would like to run it within a Docker container in Azure Kubernetes.

