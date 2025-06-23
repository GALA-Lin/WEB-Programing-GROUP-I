# WEB-Programing-GROUP-I
Cupk 2025夏 web程序设计 第一组

## 成员



| 序号 | 学号       | 姓名   | 班级       |
| :--- | :--------- | :----- | :--------- |
| 37   | 2023015509 | 胡林森 | 数据23-3班 |
| 93   | 2023016701 | 郑智   | 数据23-3班 |
| 157  | 2023015507 | 贺仁铭 | 软工23-2班 |
| 94   | 2023016901 | 周程   | 数据23-1班 |
| 139  | 2023015475 | 郭人禄 | 软工23-2   |



## 项目描述



### 项目选题



### 技术栈

- 后端：Java SpringBoot, Spring MVC, Spring Security, Mybatis
- 构建工具：Maven
- 前端：Vue3, css
- 数据库：MySQL
- 版本管理：Git
- 代码托管：GitHub
- 服务器：Docker



-----

# 当前任务：全员必做 - 本地软件部署

这个任务是所有后续工作的基础。

**目标：** 在你自己的电脑上装好所有必需的开发软件。

-----

## 安装 GitHub Desktop (代码版本管理工具的图形化界面)
去哪里下： https://desktop.github.com/
怎么装： 下载对应你操作系统（Windows/Mac）的版本。双击下载的安装包，按照提示完成安装即可。
如何验证： 安装完成后，启动 GitHub Desktop 应用。如果能正常打开并显示主界面，就说明装好了。
-----

## 安装 JDK 21 (Java 开发工具包)

  * **去哪里下：** 推荐使用 OpenJDK。访问 [https://adoptium.net/temurin/releases/](https://adoptium.net/temurin/releases/)

  * **怎么选：** 在页面上找到 "Version: 21 (LTS)"，选择你的操作系统，下载 `.msi` (Windows) 或 `.pkg` (Mac) 安装包。

  * **怎么装：** 双击安装包，一路 "Next"。在某一步它会问你是否 "Set JAVA\_HOME variable"，请务必选择 "Will be installed on local hard drive" (通常是一个小硬盘图标)，这会自动帮你配置好环境变量。

  * **如何验证：** 重启你的终端，输入：

    ```bash
    java -version
    ```

    如果能显示出 `openjdk version "21...."`，就说明装好了。

-----

## 安装 Node.js 22 (前端运行环境)

  * **去哪里下：** [https://nodejs.org/en/download/prebuilt-installer](https://nodejs.org/en/download/prebuilt-installer)

  * **怎么选：** 找到 "22.x.x LTS"，选择你的操作系统（Windows Installer `.msi` 或 macOS Installer `.pkg`）。

  * **怎么装：** 一路 "Next" 到底。

  * **如何验证：** 重启你的终端，输入：

    ```bash
    node -v
    ```

    如果能显示出 `v22.x.x`，就说明装好了。

-----

## 安装 Docker Desktop (容器化工具)

  * **去哪里下：** [https://www.docker.com/products/docker-desktop/](https://www.docker.com/products/docker-desktop/)
  * **怎么装：** 下载后双击安装。Windows 系统可能会提示你需要开启 WSL 2，根据 Docker 的指引操作即可（通常是几条命令）。安装过程可能需要重启电脑。
  * **如何验证：** 安装完成后，启动 Docker Desktop 应用。等待它左下角的图标变成绿色，并显示 "Running"，就说明好了。

-----

## 安装 IntelliJ IDEA (我们的“代码编辑器”)

  * **去哪里下：** [https://www.jetbrains.com/idea/download/](https://www.jetbrains.com/idea/download/)
  * **怎么装：** 下载 Community (社区免费版) 或 Ultimate (旗舰版)。作为学生，你们可以免费申请旗舰版，功能更强大。安装时一路 "Next"。

-----

✅ **任务零完成标志：** 以上5个软件全部安装并验证成功。

-----

