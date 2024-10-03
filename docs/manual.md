# Framework modeling tool (Astah plug-in)

- The language in the images is Japanese due to issues with changing Astah's language settings.
- Some screenshots from the provided videos/slides have been used.
- If any included images cannot be used, please replace them as necessary.

## Details
### Compatible with
- astah System Safety
    - Version: 8 (Model Version 9)

## Plugin Overview

This plugin allows you to run the Multi-view Modeling Framework for ML System (M3S) process within Astah* System Safety.
While referring to the Process Guide View, create an ML Canvas, AI Project Canvas, KAOS Goal Model, etc., and investigate under what conditions it is best to repair the ML model.
By providing a separate ML model learning mechanism in the backend, you can easily repair ML models and check the results from Astah.

<img src="./imgs/0-1.png" />

## Features

- Create an ML Canvas
- Create an AI Project Canvas
- Train a new version of the ML Model.
- Validate whether a version of the ML Model satisfies the requirements or not.
- Repair ML model based on specification in assurance case.

## Network Setup
To set a network connection with the backend side, do the following:

1. Clone the repository
2. Access the [SSHFramework.java](src/main/java/ai/engineering/pipeline/SSHConnector.java).
3. Set the IP address, username, password, and port in the following section:
   ```
   //Set the host IP, username, password, and port for training server
    static String host = "";
    static String user = "";
    static String password = "";
    static String port = ""
   ```
4. Set the local directory for temporary files to be stored. The default is the D drive root folder.
   ```
   //Set the local folder to hold temporary files
   static String local = "D:";
   ```
5. Compile the plugin.

## Installation Instructions
1. Drop the .jar file in the releases into the opened Astah System Safety instance to install.
2. Restart the Astah System Safety for the installation to be implemented.
3. The installation is successful if you see ai.engineering.frameworktool in installed plugin list.


<img src="./imgs/0-2.png" />

## How to use

Open the closed pane at the bottom of the screen.
Up until section 6, we will follow the steps in the Process Guide View, adding supplementary information as appropriate.

<img src="./imgs/0-3.png" />

By following the Process Guide View, you will enter the contents of each item in the metamodel below in order.
<img src="../metamodels/Metamodel.png" />

### 1. Develop AI Project Canvas

References: [https://towardsdatascience.com/introducing-the-ai-project-canvas-e88e29eb7024]

Use AI Project Canvas to analyze the project by focusing on its "Value".
There are nine items to fill in: Value Proposition, Customers, Stakeholders, Integration, Output, Data, Skills, Cost, and Revenue.
These correspond to the grey blocks in the metamodel.

Create requirement SysML element with the following stereotype to mark their position in the AI Project Canvas:

| Stereotype          | Position in the canvas |
| ------------------- | ---------------------- |
| AI.ValueProposition | Value Proposition      |
| AI.Customers        | Customers              |
| AI.Stakeholders     | Stakeholders           |
| AI.Integration      | Integration            |
| AI.Output           | Output                 |
| AI.Data             | Data                   |
| AI.Skills           | Skills                 |
| AI.Cost             | Cost                   |
| AI.Revenue          | Revenue                |

| | |
|---|---|
|<img src="./imgs/1-1.png" />| <img src="./imgs/1-2.png" />|

### 2. Develop Machine Learning Canvas

References: [https://www.ownml.co/machine-learning-canvas]

Use the ML Canvas to analyze the project by focusing on the "ML Task" to be performed.
There are 10 items to fill in: Value Proposition, Prediction Task, Decisions, Impact Simulation, Making Prediction, Building Models, Data Collection, Data Sources, Features, and Monitoring.
These correspond to the green blocks in the metamodel.

Create requirement SysML element with the following stereotype to mark their position in the AI Project Canvas:

| Stereotype          | Position in the canvas |
| ------------------- | ---------------------- |
| ML.ValueProposition | Value Proposition      |
| ML.Decision         | Decision               |
| ML.PredictionTask   | Prediction Task        |
| ML.ImpactSimulation | Impact Simulation      |
| ML.MakingPrediction | Making Prediction      |
| ML.DataCollection   | Data Collection        |
| ML.DataSources      | Data Sources           |
| ML.BuildingModels   | Building Models        |
| ML.Features         | Features               |
| ML.LiveMonitoring   | Live Monitoring        |

<img src="./imgs/2-1.png" />

### 3. Develop KAOS Goal Model

Let's use the KAOS Goal Model to break it down into the "Goal" to be achieved and the requirement necessary to achieve them.
Determine the Top Goals based on the contents of the ML Canvas and break them down into the goals of the ML Component.
These correspond to the orange blocks in the metamodel.

<img src="./imgs/3-1.png" />

|Models created with ML Canvas can be reused|You can set conditions that ML Components must achieve|
|---|---|
|<img src="./imgs/3-2.png" /> |<img src="./imgs/3-3.png" />|

You can also use the form at **Pipeline Performance View** to set the conditions that ML Components must achieve. All conditions that each ML Components must achieve is summarized in the **Pipeline Monitoring Summary**.

|Pipeline Performance View(Setting Expected Performance of a goal.)|Pipeline Monitoring Summary(Summary of expected performances sets.)|
|---|---|
|<img src="./imgs/7-x-1.png" /> |<img src="./imgs/7-x-2.png" />|

### 4. Develop Architectural Diagram

Let's visualize the "Architecture" by enumerating and associating the necessary ML and non-ML elements to the Architectural Diagram.
These correspond to the red blocks in the metamodel.

|Block definition diagram|Internal block diagram|
|---|---|
|<img src="./imgs/4-1.png" /> |<img src="./imgs/4-2.png" />|

### 5. Develop STAMP/STPA Analysis

Let's use STAMP/STPA Analysis to list which "Safety" measures we want to improve and what indicators we can use to quantify them.
For information on how to perform STAMP/STPA Analysis, please refer to the Astah official documentation, which includes tutorials.
In the metamodel, this corresponds to the yellow block on the right.

|Identification of losses, hazards, and safety constraints|Building a control structure|
|---|---|
|<img src="./imgs/5-1.png" />|<img src="./imgs/5-2.png" />|

|Unsafe Control Action (UCA) Identification |Countermeasure Consideration|
|---|---|
|<img src="./imgs/5-3.png" />|<img src="./imgs/5-4.png" />|

### 6. Develop a Safety Case Analysis

Let's use the KAOS Goal Model to consider how to "strengthen" the safety of ML models.
These correspond to the blue blocks in the metamodel.

<img src="./imgs/6-1.png" />

### 7. eAI Framework tools

In this section, we will finally train and repair the ML model.
We will introduce the five items in this image.
Please note that some tools require connection to backend.
In the metamodel, these correspond to the yellow blocks at the bottom.

<img src="./imgs/7-0-1.png" width="300" />

### 7.1. Train and Test ML models (Requires connection to backend)

Train a new version of ML model at server.
The input items at the top are the parameters of the new version of the ML model.
Click the button below to start training at backend.

<img src="./imgs/7-1-1.png" width="300" />

### 7.2. Fetch model performance (Requires connection to backend)

Retrieve ML performance test data and propagate red color (indicating failure) from goals with failed expected performance. 

|Before fetch|After fetch|
|---|---|
|<img src="./imgs/7-2-1.png" />| <img src="./imgs/0-1.png" />|

### 7.3. Repair ML Model (Requires connection to backend)

Execute DNN repair using configured repair configuration. 
In the New model name field, you can set the New Name for repaired version of ML Model.
In the Base model version field, you can set the List of existing version of ML model to be used as the base.
In the Existing configuration field, a list of repair configurations is displayed.
Press the Repair button at the bottom to start the repair process at backend.

<img src="./imgs/7-4-1.png" width="300" />

Repair configurations can be set/checked in the following view:
|Repair Configuration View (Set a solution node as repair configuration.)| Repair Configuration Sumamry (Summary of DNN repair configurations.)|
|---|---|
|<img src="./imgs/7-4-2.png" /> |<img src="./imgs/7-4-3.png" />|

And if an item that was originally below the target value now exceeds the target value after repair is performed, it will no longer be marked red.

|Before Repair|After Repair|
|---|---|
|<img src="./imgs/7-4-4.png" />|<img src="./imgs/7-4-5.png" />|

