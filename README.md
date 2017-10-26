# アプリ名
ぶしも！
# 概要    
時間帯によって音量を制御する

# インフラ環境
- FireBase
    - Auth
    - Database

# 開発環境
- GitHub
- Circle CI
- Slack
- DeployGate

# DB設計
未設計

# 画面遷移図
未提案

# 画面設計図
未設計

# 担当
- 大前
    - 要件定義
    - インフラ環境設定
    - 開発環境構築
    - DB設計
    - Androidアプリ開発レビュアー
- 木村
    - 要件定義
    - 画面遷移図作成
    - 画面設計作成
    - DB設計
    - Androidアプリ開発

# 開発
## コード規約
|対象|規約|例|
|:-----------:|:------------:|:------------:|
|class名|CamelCase|MainActivity|
|変数|lowCamelCase|textView|
|view id|snake_case|thumbnail_image|
|string resource id|snake_case|app_name|
|Drawable|snake_case|white_arrow|
|style|lowCamelCase|colorAccent|
## 使用言語
- Java
## 使用ライブラリ
## パッケージ構成
- model: データに関するパッケージ  
	- entity: データの型に関するパッケージ  
	- network: 通信部分に関するパッケージ
- presenters: viewまたはcontrollerに関するパッケージ
	- activity: activityに入れるパッケージ
	- fragment: fragmentを入れるパッケージ
	- adapter: adapter類を入れるパッケージ
- view: customviewに関するパッケージ






