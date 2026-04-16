@echo off
echo ========================================
echo Milvus 向量数据库快速启动脚本
echo ========================================
echo.

REM 检查 Docker 是否安装
docker --version >nul 2>&1
if %errorlevel% neq 0 (
    echo [错误] 未检测到 Docker，请先安装 Docker Desktop
    echo 下载地址：https://www.docker.com/products/docker-desktop
    pause
    exit /b 1
)

echo [√] Docker 已安装
echo.

REM 检查 Milvus 容器是否已存在
docker ps -a --filter "name=milvus-standalone" --format "{{.Names}}" | findstr "milvus-standalone" >nul 2>&1
if %errorlevel% equ 0 (
    echo [信息] Milvus 容器已存在
    docker ps --filter "name=milvus-standalone" --format "{{.Status}}" | findstr "Up" >nul 2>&1
    if %errorlevel% equ 0 (
        echo [√] Milvus 正在运行
    ) else (
        echo [操作] 启动 Milvus 容器...
        docker start milvus-standalone
    )
) else (
    echo [操作] 首次安装 Milvus...
    echo.
    
    REM 创建数据目录
    if not exist "%TEMP%\milvus" (
        mkdir "%TEMP%\milvus"
        echo [√] 创建数据目录：%TEMP%\milvus
    )
    
    REM 拉取镜像
    echo [操作] 拉取 Milvus 镜像...
    docker pull milvusdb/milvus:v2.3.0
    
    REM 启动容器
    echo [操作] 启动 Milvus 容器...
    docker run -d ^
      --name milvus-standalone ^
      -p 19530:19530 ^
      -p 9091:9091 ^
      -v "%TEMP%\milvus":/var/lib/milvus ^
      milvusdb/milvus:v2.3.0
    
    echo [√] Milvus 容器已启动
)

echo.
echo ========================================
echo Milvus 服务状态
echo ========================================
docker ps --filter "name=milvus-standalone" --format "容器名：{{.Names}}\n状态：{{.Status}}\n端口：{{.Ports}}"
echo.
echo 访问地址:
echo - Milvus 服务：http://localhost:19530
echo - 管理界面：http://localhost:9091
echo.
echo ========================================
echo 提示：
echo - 查看日志：docker logs -f milvus-standalone
echo - 停止服务：docker stop milvus-standalone
echo - 重启服务：docker restart milvus-standalone
echo ========================================
echo.
pause
