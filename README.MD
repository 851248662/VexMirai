# VexMirai
想拥有你的群幻翼嘛？
使用VexMirai，在深夜对活跃的人们发送美食图片！

# Usage
```yaml
keywords: #关键词列表
  - "来点吃的" # 支持正则表达式
Trigger: 15 #在阈值内说了多少句话触发Vex?
Cycle: 3 #多少分钟作为一次阈值？
CacheDelay: 300 #多少秒更新一次图片列表缓存？
Site: "https://www.xinshipu.com" #从哪爪巴 ,HTTPS Required
## 正则表达式:(<img src=\"\/\/)(.*jpg?)，目前无法修改:（懒
```