# Raptor-tutorial
Tutorial for Raptorbox - an open-source lightweight IoT platform 

## Pre-requisites

[Raptor](https://github.com/raptorbox/raptor) 
(Check the [Getting Started](https://github.com/raptorbox/raptor#getting-started) to set it up)

### Installation
1. Download the project.
2. Build the maven project by running “mvn clean package” in root folder
3. Replace username, password [here](https://github.com/raptorbox/raptor-tutorial/blob/master/src/main/resources/application.properties) with the one you created with Raptor (mentioned in pre-requisites). 
4. Run Raptor in case of local instance, otherwise change the url in the above mentioned file to the one where your raptor instance is running.
5. Run [RaptorTutorial](https://github.com/raptorbox/raptor-tutorial/blob/master/src/main/java/createnet/raptorbox/quickstart/RaptorTutorial.java). It includes some simple functionality like login, creating device, stream and channel, updating them, pushing, pulling and droping data (records) of device etc. The id of the device created here will be used later for running the client.
6. In order to subscribes and get updates of device, replace the device id [here](https://github.com/raptorbox/raptor-tutorial/blob/master/src/main/java/createnet/raptorbox/quickstart/client/Client.java) with the one you created above or with your desired one.
7. Run the [Client](https://github.com/raptorbox/raptor-tutorial/blob/master/src/main/java/createnet/raptorbox/quickstart/client/Client.java). 
