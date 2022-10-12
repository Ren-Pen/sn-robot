<h1 align="center">SlimeNano</h1>

<p align="center">一个通用聊天机器人框架，基于 JAVA 开发</p>

### 注意：该项目仅用于学习！


### 许可证


    Copyright (C) 2022-2023  Ren-Pen and contributors.

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as published
    by the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.


控制台界面桥 : https://github.com/Ren-Pen/sn-bridge-cli

Mirai 模块 : https://github.com/Ren-Pen/sn-mirai-model

### 如何安装

slimenano的目录结构如下：

slimenano
- cache 【运行生成】缓存
- data 【运行生成】程序数据、插件数据
- extension 扩展
  - lib 扩展库
- lib 核心库
- logs 【运行生成】日志
- plugins 【运行生成】插件
- tmp 【运行生成】临时
- validation 【运行生成】额外的xsd验证文件

克隆该项目，打开 pom.xml，将 sn-bot.dir 修改为自己想要安装到的位置，保存后运行 `mvn install`

克隆想要使用的平台对应的实现模块 修改 parent-pom.xml 中的 sn-bot.dir 与先前更改的保持一致，保存后运行 `mvn install`

克隆想要使用的界面桥 修改 parent-pom.xml 中的 sn-bot.dir 与先前更改的保持一致，保存后运行 `mvn install`

安装完成。


### 如何运行
Windows:
```shell
java -Dlog4j.skipJansi=false -cp lib/*;extension/*;extension/lib/*;. com.slimenano.framework.RobotApplication
```

Linux:
```shell
java -Dlog4j.skipJansi=false -cp lib/*:extension/*:extension/lib/*:. com.slimenano.framework.RobotApplication
```
