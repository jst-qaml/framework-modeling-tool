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

<img src="./imgs/img21.png" />

## Features

- ML Canvasを作成する
- AI Project Canvasを作成する
- MLモデルをrepairする仕組み (TODO: より細かい要素に分解する)

## Installation Instructions

1. Clone the framework-modeling-tool repository.
2. Drop the .jar file in the releases into the opened Astah System Safety instance to install.
3. Restart the Astah System Safety for the installation to be implemented.
4. The installation is successful if you see ai.engineering.frameworktool in installed plugin list.

<img src="./imgs/img1.png" />

## How to use

画面下部に閉じているペインがあるので、それを開いてください。
本節では、Process Guide Viewの手順に従いながら、適宜補足情報を追加していきます。

<img src="./imgs/img2.png" />

### 1. Develop AI Project Canvas

<img src="./imgs/img3.png" />
<img src="./imgs/img4.png" />

### 2. Develop Machine Lerarning Canvas

<img src="./imgs/img5.png" />


### 3. Develop KAOS Goal Model

<img src="./imgs/img6.png" />
<img src="./imgs/img17.png" />
<img src="./imgs/img16.png" />

### 4. Develop Architectural Diagram

<img src="./imgs/img7.png" />
<img src="./imgs/img11.png" />

### 5. Develop STAMP/STPA Analysis

<img src="./imgs/img8.png" />

### 6. Develop Safety Case Analysis

<img src="./imgs/img10.png" />

### 7. Title is unknown

<img src="./imgs/img13.png" />

### 7.x. Train and Test ML models
<img src="./imgs/img18.png" />

### 7.x. Fetch model performance
<img src="./imgs/img20.png" />
<img src="./imgs/img21.png" />

### 7.x Repair Configuration View
<img src="./imgs/img24.png" />
