# Case Study

We prepared the case study of traffic sign image classification to demonstrate how M3S works with our plugin. Please refer to [our publication](https://link.springer.com/article/10.1007/s11219-024-09687-z) for the details.

## Case Study Description

<img src="/docs/imgs/Case Study Illustration.png" />

The case study is based on object classification ML models for autonomous driving cars (ADV). Here, the scope of the classification task is limited to traffic sign classification. The inputs for the ML model classification are color images from an embedded camera system as the sensors of the overall ADV system. The classification result is sent to the decision-making ML model as a decision-making input for the car’s control system. 

ADV is required to work at level three of vehicle autonomy. In level three conditions, the autonomous part returns the driving responsibility to the driver when ADV is operated outside the preferable domain. To train and test the model, we used the publicly available German Traffic Sign Recognition Benchmark (GTSRB) dataset (Stallkamp et al., 2012, 2011). The GTSRB dataset consists of images of German traffic signs that fit the case study.

The case study considers two operation domains. The first one is the highway. Because this domain is free of pedestrians and bikes, traffic signs indicating such objects are non-existent. The second one is suburban roads, where pedestrians and bikes are more prevalent. The highway domain is prioritized from an economic standpoint, whereas the suburban road domain is preferable from the users' standpoint. The JAMA Framework Japan Automobile Manufacturers Association (2021) and Aurora’s safety case framework for ADVsFootnote 3 serve as the basis to ensure that the case study reflects the real world as much as possible. To fit the ML model to both cases, DNN repair may be utilized to improve the performance of important classes. However, if no version of trained ML models satisfies both cases, the highway domain is prioritized. The configuration of the repair process must reflect such concerns.

## Available files

The following files are provided in the [case study folder](https://github.com/jst-qaml/framework-modeling-tool/tree/main/case%20study):

| File (.axmz)                    | Description                                     |
| ------------------------------- | ----------------------------------------------- |
| Case study                      | First version of analysis                       |
| Case study_configured_prerepair | Second version with requirements validated      |
| Case study_repair_aggresive     | Third version with aggressive repair implemented |
| Case study_repair_balanced      | Third version with aggressive repair implemented |
| Case study_update               | Final version with requirements updated         |

## Results

Through the case study, we have proved several findings as follows:

1. Traceability through integrated metamodel.

This case study showed that the integrated metamodel of M3S managed to guide and connect different elements from different analysis models. Resulting in not only a comprehensive but also a traceable process of analysis, including the refinement of the analysis part.

| Tracing the ML Canvas into Architecture Diagram | Tracing the goal of Goal Model into Assurance Case into STAMP/STPA |
| ---- | ----- |
| <img src="/docs/imgs/Case Study Step 2.png" /> | <img src="/docs/imgs/Case Study Step 5.png" /> |


2. Integrated training, validation, and improvement pipeline.

This case study also showed that the integrated pipeline facilitated a feedback loop between the analysis and ML training parts. This allows continuous improvement on both sides.

| Tracing the ML Canvas into Architecture Diagram |
| ---- |
| <img src="/docs/imgs/Case Study Step 3-2_highres.png" /> |
