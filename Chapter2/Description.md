# Android Chapter_2
## 本次作业主要完成一个热度排行榜：
* 共显示三项内容：序号、标题、热度
* 显示的数据不超过20条
* 支持insert、delete、change三个功能
* 每次刷新会重置更新时间

## 实现方法
### 排行榜窗体为垂直线性布局，从上至下依次为TextView（显示更新时间），RecyclerView（排行榜内容）和一个水平线性布局（三个Button）。
### 每次排行榜内容发生变化后都会重置更新时间，当前时间通过Date库取得。
### 排行榜实现与上课所讲方法一致，只在LinearLayout中设置了3个TextView用于表示更多信息，并在Adapter中进行了相应更改。
### 插入、删除与更改功能通过startActivityForResult方法实现，重载onClick方法，用显式Intent调用相应Activity，返回时设置不同resultCode来区分不同activity。