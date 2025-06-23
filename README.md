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

#  步骤一：项目开发环境配置规范

## 1. 概述

本规范的核心原则是将开发工具进行本地化安装，同时将项目依赖的外部服务进行容器化管理。

## 2. 本地开发环境配置 (Local Development Environment Setup)

### 2.1. Git

* **定义：** 分布式版本控制系统。
* **官方下载地址：** [https://git-scm.com/downloads](https://git-scm.com/downloads)
* **操作规程：** 下载对应操作系统的最新稳定版安装包。在安装过程中，应使用所有默认设置，无需进行任何修改。

### 2.2. JDK (Java Development Kit)

* **定义：** Java 语言的软件开发工具包。
* **指定版本：** JDK 21 (LTS)
* **官方下载地址：** [https://adoptium.net/temurin/releases/](https://adoptium.net/temurin/releases/) (Temurin 版)
* **操作规程：**
    1.  从上述指定链接下载与操作系统匹配的 `JDK 21` 版本（`.msi` for Windows, `.pkg` for macOS）。
    2.  在安装过程中，必须确保已勾选并执行 **"Add to PATH"** 和 **"Set JAVA_HOME variable"** 两个关键步骤，以完成系统级环境变量的配置。

* **强制性要求与修正规程：**
    * **禁止**使用任何 IDE（如 IntelliJ IDEA）内置的 JDK 下载及管理功能。此举是为了保证所有成员的编译及运行环境绝对一致。
    * 若有成员已通过 IDE 下载了 JDK，必须执行以下**修正规程**：
        a.  首先，仍需按照上述操作规程，完成系统级 JDK 的下载与安装。
        b.  其次，打开 IntelliJ IDEA，进入 `File -> Project Structure... -> Project` 设置。
        c.  在 `SDK` 下拉菜单中，通过 `Add SDK -> JDK...` 选项，手动指定到系统级 JDK 的安装路径（例如 `C:\Program Files\Eclipse Adoptium\jdk-21...` 或 `/Library/Java/JavaVirtualMachines/temurin-21.jdk/...`）。
        d.  将新添加的、不含任何 IDE 标记的系统级 JDK 设置为项目默认 SDK。

### 2.3. Node.js

* **定义：** JavaScript 运行环境，用于前端项目构建与开发。
* **指定版本：** Node.js 22 (LTS)
* **官方下载地址：** [https://nodejs.org/en/download/prebuilt-installer](https://nodejs.org/en/download/prebuilt-installer)
* **操作规程：**
    1.  从上述链接下载与操作系统匹配的长期支持版 (LTS) 安装程序（`Windows Installer (.msi)` 或 `macOS Installer (.pkg)`）。
    2.  **禁止**使用下载页面中展示的 Docker 相关安装选项。前端开发工具链必须在本地主机上运行，以确保开发服务器的性能与热重载功能的正常工作。
    3.  执行安装程序并使用所有默认设置。

### 2.4. Docker Desktop

* **定义：** 容器化应用管理平台。
* **官方下载地址：** [https://www.docker.com/products/docker-desktop/](https://www.docker.com/products/docker-desktop/)
* **操作规程：**
    1.  下载并安装最新稳定版。
    2.  安装完成后，必须启动 Docker Desktop 应用程序，并确认其状态为“正在运行 (Running)”。

### 2.5. IntelliJ IDEA

* **定义：** 项目指定集成开发环境。
* **官方下载地址：** [https://www.jetbrains.com/idea/download/](https://www.jetbrains.com/idea/download/)
* **操作规程：** 推荐安装 Ultimate (旗舰版) 以获得完整的工具支持。团队成员可申请学生免费授权。

## 3. 环境配置验证

为确保所有工具已正确安装并配置到系统路径，请打开**操作系统原生终端**（例如，Windows 的 `PowerShell` 或 macOS 的 `Terminal`），并逐一执行以下命令进行验证。

1.  **验证 Git:**
    ```bash
    git --version
    ```
    *预期输出：显示 Git 的版本号。*

2.  **验证 Java (JDK):**
    ```bash
    java -version
    ```
    *预期输出：显示 `openjdk version "21.x.x"` 等信息。*
    * **关键验证点：** 此命令必须在操作系统原生终端中成功执行。若失败，意味着 JDK 环境变量配置有误，必须返回 `2.2` 节重新执行安装与配置流程。*

3.  **验证 Node.js:**
    ```bash
    node -v
    ```
    *预期输出：显示 `v22.x.x`。*

4.  **验证 Docker:**
    ```bash
    docker --version
    ```
    *预期输出：显示 Docker 的版本号。*

---
环境配置规范说明完毕。所有成员必须在项目启动前完成并验证上述所有步骤。如在配置过程中遇到任何无法自行解决的问题，请及时联系项目负责人。
