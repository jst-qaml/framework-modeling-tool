# Framework modeling tool (Astah plug-in)

- 画像の大きさは、後程調整します。
- 画像中の言語が日本語なのは、Astahの言語設定変更方法が動かないためです。

## Details
### Compatible with
- astah System Safety
    - Version: ???

## Plugin Overview

本プラグインは、astah内でMLモデルのrepairを効率的に行うためのプラグインです。
Process Guide Viewを参照しながら、ML Canvas, AI Project Canvas, KAOS Goal Modelなどを作成していき、どのような条件でMLモデルを修正すると良いかを調査していきましょう。
バックエンドに別途MLモデルの学習機構を用意することで、astah上からMLモデルの修正と結果の確認を容易に行えます。

<img src="./imgs/0-1.png" />

以下のメタモデルの各項目の内容を埋めていくように進めます。
<img src="../metamodels/Metamodel.png" />

## Features

- ML Canvasを作成する
- AI Project Canvasを作成する
- MLモデルをrepairする仕組み (TODO: より細かい要素に分解する)

## Installation Instructions

1. Clone the framework-modeling-tool repository.
2. Drop the .jar file in the releases into the opened Astah System Safety instance to install.
3. Restart the Astah System Safety for the installation to be implemented.
4. The installation is successful if you see ai.engineering.frameworktool in installed plugin list.

TODO: pipelineと接続する手順 (?)

<img src="./imgs/0-2.png" />

## How to use

画面下部に閉じているペインがあるので、それを開いてください。
本節では、Process Guide Viewの手順に従いながら、適宜補足情報を追加していきます。

<img src="./imgs/0-3.png" />

### 1. Develop AI Project Canvas

AI Project Canvasを用いて、そのプロジェクトの「価値」にフォーカスして分析しましょう。埋める項目は、Value Proposition, Customers, Stakeholders, Integration, Output, Data, Skills, Cost, Revenueの9項目です。
metamodelでは灰色のブロックに対応します。
(TODO: 有用なリンク等)

| | |
|---|---|
|<img src="./imgs/1-1.png" />| <img src="./imgs/1-2.png" />|

### 2. Develop Machine Lerarning Canvas

ML Model Canvasを用いて、そのプロジェクトにおいて実施する「ML Task」にフォーカスして分析しましょう。埋める項目は、Value Proposition, Prediction Task, Decisions, Impact Simulation, Making Prediction, Building Models, Data Collection, Data Sources, Features, Monitoringの10項目です。
metamodelでは緑色のブロックに対応します。
(TODO: 有用なリンク等)

<img src="./imgs/2-1.png" />


### 3. Develop KAOS Goal Model

KAOS Goal Modelを用いて、達成すべき「目標」と、そのために必要な「要件」に分解していきましょう。ML Canvasの内容を踏まえてTop Goalsを求めて、ML Componentの目標へと分解していきましょう。
metamodelでは橙色のブロックに対応します。
(TODO: 有用なリンク等)

<img src="./imgs/3-1.png" />

| | |
|---|---|
|<img src="./imgs/3-2.png" /> |<img src="./imgs/3-3.png" />|

### 4. Develop Architectural Diagram

Architectural Diagramに対して、必要なML要素や非ML要素を列挙/関連付けていき、「構造」を可視化しましょう。
metamodelでは赤色のブロックに対応します。
(TODO: 有用なリンク等)

<img src="./imgs/4-1.png" />

### 5. Develop STAMP/STPA Analysis

STAMP/STPA Analysisを用いて、どのような「問題」が起きる可能性があり、どのような「指標」を用いることで解決/未解決かを判定できるのかを列挙していきましょう。STAMP/STPA Analysisをどのように行うのかについては、公式ドキュメントにチュートリアルなどが載っていますので参照してください。
metamodelでは右側の黄色のブロックに対応します。

<img src="./imgs/5-1.png" />

### 6. Develop Safety Case Analysis

KAOS Goal Modelを用いて、MLモデルの安全性を「強化」するためにはどうすれば良いかを検討していきましょう。
metamodelでは青色のブロックに対応します。

<img src="./imgs/6-1.png" />

### 7. Title is unknown

metamodelでは下側の黄色のブロックに対応します。

<img src="./imgs/7-1.png" />

### 7.x. Train and Test ML models

<img src="./imgs/7-2.png" />

### 7.x. Fetch model performance

|Before fetch|After fetch|
|---|---|
|<img src="./imgs/7-4.png" />| <img src="./imgs/0-1.png" />|

### 7.x Repair Configuration View

<img src="./imgs/7-3.png" />
