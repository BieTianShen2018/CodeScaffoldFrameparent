FROM centos:7
MAINTAINER tyn <tanye.tyn@alibaba-inc.com>
# 安装打包必备软件
RUN yum install -y wget unzip telnet lsof net-tools bind-utils

# 准备 JDK/Tomcat 系统变量
ENV JAVA_HOME /usr/lib/jvm/java
ENV PATH $PATH:$JAVA_HOME/bin
ENV CLASSPATH=.:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar
ENV ADMIN_HOME /home/admin

# 设置 EDAS-Container/Pandora 应用容器版本
ENV EDAS_CONTAINER_VERSION V3.5.4
LABEL pandora V3.5.4

# 创建 JAVA_HOME 软链接
RUN if [ ! -L "${JAVA_HOME}" ]; then mkdir -p `dirname ${JAVA_HOME}` && ln -s `readlink -f /usr/lib/jvm/java` ${JAVA_HOME}; fi

# 根据环境变量，下载安装 EDAS Contaienr/Pandora 应用容器版本
RUN mkdir -p ${CATALINA_HOME}/deploy
RUN wget http://edas-hz.oss-cn-hangzhou.aliyuncs.com/edas-plugins/edas.sar.${EDAS_CONTAINER_VERSION}/taobao-hsf.tgz -O /tmp/taobao-hsf.tgz && \
    tar -xvf /tmp/taobao-hsf.tgz -C ${CATALINA_HOME}/deploy/ && \
    rm -rf /tmp/taobao-hsf.tgz

# 下载安装 OpenJDK
RUN yum -y install java-1.8.0-openjdk-devel
RUN mkdir -p /home/admin/app/
# 拷贝jar，修改成自己项目的jar包，路径相对根目录
ARG JAR_LOCAL_PATH=amway-center-boot/target/amway-center-boot.jar
COPY ${JAR_LOCAL_PATH}  /home/admin/app/amway-center-boot.jar
# 增加容器内中⽂支持
ENV LANG="en_US.UTF-8"
# 增强 Webshell 使⽤体验
ENV TERM=xterm

# doom上传
# ARG COMMON_FILE=sdk_3.5.10/sdk/clouddoom-loader.jar
# COPY ${COMMON_FILE} /home/admin/doom/clouddoom-loader.jar
#-javaagent:/home/admin/doom/clouddoom-loader.jar=appId:1219#clientKey:DKA3UkEStY0B8osS#endPoint:https://doom.rdc.aliyun.com

# 将启动命令写入启动脚本 start.sh
RUN mkdir -p ${ADMIN_HOME}

RUN echo 'java -jar ${CATALINA_OPTS} -Djava.security.egd=file:/dev/./urandom -Dcatalina.logs=${CATALINA_HOME}/logs -Dpandora.location=${CATALINA_HOME}/deploy/taobao-hsf.sar   "/home/admin/app/amway-center-boot.jar" --server.context-path=/ --server.port=8080 --server.tomcat.uri-encoding=ISO-8859-1 --server.tomcat.max-threads=400'> /home/admin/start.sh && chmod +x /home/admin/start.sh
WORKDIR ${ADMIN_HOME}
CMD ["/bin/bash", "/home/admin/start.sh"]