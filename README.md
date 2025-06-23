#注意，请在最上面切换到dev，dev还没并入main内容中

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

# 步骤一：项目开发环境配置规范

## 1. 概述

本规范的核心原则是将开发工具进行本地化安装，同时将项目依赖的外部服务进行容器化管理。

## 2. 本地开发环境配置 

### 2.1. GitHub Desktop (代码版本管理)

* **定义：** 图形化 Git 客户端，是本项目指定的代码版本管理工具。
* **官方下载地址：** [https://desktop.github.com/](https://desktop.github.com/)
* **操作规程：**
    1.  从上述链接下载并安装应用程序。
    2.  首次启动时，程序会引导你登录。**必须使用你的 GitHub 账户进行登录**，且此账户需已被添加为本项目的协作者。
    3.  登录后，请根据指引配置你的用户名和邮箱，通常会自动从你的 GitHub 账户同步。

### 2.2. JDK (Java Development Kit)

* **定义：** Java 语言的软件开发工具包。
* **指定版本：** JDK 21 (LTS)
* **官方下载地址：** [https://adoptium.net/temurin/releases/](https://adoptium.net/temurin/releases/) (Temurin 版)
* **操作规程：**
    1.  从上述指定链接下载与操作系统匹配的 `JDK 21` 版本（`.msi` for Windows, `.pkg` for macOS）。
    2.  在安装过程中，必须确保已勾选并执行 **"Add to PATH"** 和 **"Set JAVA_HOME variable"** 两个关键步骤，以完成系统级环境变量的配置。

    ![image](https://github.com/user-attachments/assets/26289854-42ee-454f-8f43-6a389e8f0945)

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

## 3. GitHub Desktop 核心工作流程

所有代码的提交与同步，均须通过 GitHub Desktop 完成。

### 3.1. 克隆 (Clone) 远程仓库

此操作仅需执行一次。
1.  打开 GitHub Desktop。
2.  在主界面选择 "Clone a repository from the Internet..." 或通过菜单栏 `File -> Clone Repository...`。
3.  在弹出的窗口中，选择 `GitHub.com` 标签页。列表中应能看到本项目仓库，选中它。
4.  在 "Local path" 字段，选择一个用于存储项目代码的本地文件夹。
5.  点击 `Clone` 按钮。

### 3.2. 日常开发标准流程

此流程为每次开发新功能或修复问题时的标准操作。

1.  **同步 `develop` 分支**
    * 在开始编码前，必须先同步云端的最新代码。
    * 在 GitHub Desktop 顶部，将 "Current Branch" 切换到 `develop` 分支。
    * 点击右上角的 `Fetch origin` 按钮。如果云端有更新，该按钮会变为 `Pull origin`，再次点击它即可将最新代码拉取到本地。

2.  **创建新功能分支**
    * 所有开发工作必须在新分支上进行，严禁直接在 `develop` 或 `main` 分支上修改。
    * 点击 "Current Branch" (当前分支) 下拉菜单，选择 `New Branch`。
    * 为新分支命名，命名需遵循 `feature/功能描述` 或 `fix/问题描述` 的规范（例如 `feature/user-login`）。
    * 确认 "From branch" (基于分支) 选项为 `develop`。
    * 点击 `Create Branch`。

3.  **提交 (Commit) 本地更改**
    * 在 IntelliJ IDEA 中完成代码的编写和修改。
    * 切换回 GitHub Desktop，左侧 "Changes" 区域会列出所有已修改的文件。
    * 在左下角的 "Summary" (标题) 框中，必须填写符合 **Conventional Commits** 规范的摘要，例如 `feat(login): 实现用户登录接口`。
    * 点击蓝色的 `Commit to feature/user-login` 按钮。

4.  **推送 (Push) 分支到远程**
    * 提交后，更改仅保存在本地。需将其推送到 GitHub 云端仓库。
    * 点击程序顶部的 `Publish branch` (首次推送) 或 `Push origin` (后续推送) 按钮。

5.  **创建 Pull Request (PR)**
    * 分支推送到远程后，GitHub Desktop 通常会显示一个带有 `Create Pull Request` 按钮的提示条，点击它。
    * 浏览器将自动打开 GitHub 网站的 PR 创建页面。
    * 确认基座分支为 `develop`，对比分支为你的功能分支。
    * 填写必要的说明后，点击 `Create Pull Request`，并通知项目负责人进行代码审查 (Code Review)。

## 4. 环境配置验证

为确保所有工具已正确安装并配置到系统路径，请打开**操作系统原生终端**（例如，Windows 的 `PowerShell` 或 macOS 的 `Terminal`），并逐一执行以下命令进行验证。

1.  **验证 Java (JDK):**
    ```bash
    java -version
    ```
    *预期输出：显示 `openjdk version "21.x.x"` 等信息。*
    * **关键验证点：** 此命令必须在操作系统原生终端中成功执行。若失败，意味着 JDK 环境变量配置有误，必须返回 `2.2` 节重新执行安装与配置流程。*

2.  **验证 Node.js:**
    ```bash
    node -v
    ```
    *预期输出：显示 `v22.x.x`。*

3.  **验证 Docker:**
    ```bash
    docker --version
    ```
    *预期输出：显示 Docker 的版本号。*
    ![image](https://github.com/user-attachments/assets/910c7c44-5e05-472b-9ccb-3e0c356d4623)
    不一样可能是环境变量配错了，问ai跟着改改试试
   
---

环境配置规范说明完毕。所有成员必须在项目启动前完成并验证上述所有步骤。如在配置过程中遇到任何无法自行解决的问题，请及时企业微信联系。
