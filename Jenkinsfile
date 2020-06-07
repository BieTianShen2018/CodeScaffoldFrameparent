pipeline {
    agent {
            //label "tyn-jenkins-jnlp"
            label "master"
     }
    options {
        //仅保留最近10次的构建记录
        buildDiscarder(logRotator(numToKeepStr: '10'))
        //设置管道运行的超时时间，在此之后，Jenkins将中止管道
        timeout(time: 20, unit: 'MINUTES')
        //失败重试次数
        //retry(2)
        //输出时间戳
        timestamps()
    }
    environment {
	   //获取当前目录
       BASE_DIR = "${env.WORKSPACE}"
       //BASE_DIR = BASE_DIR.trim()

       //确保@@TODO都有填写
	   // 阿里云账号密钥 （可以配置成jenkins的全局变量，避免在jenkinsfile里面直接显示）
       ACCESS_KEY="@@TODO"
       ACCESS_SECRET="@@TODO"
       // 区域与EDAS应用ID
       REGION="cn-shanghai"
       APP_ID="@@TODO"

	   // 配置基础的指令
	   ALIYUN_CMD="${BASE_DIR}/aliyun"
	   JQ_CMD="${BASE_DIR}/jq"
	   BASE_CMD_EDAS="${ALIYUN_CMD}  edas "
	   BASE_CMD_OSS="${ALIYUN_CMD} oss "
	   READINESS='{"initialDelaySeconds":10,"periodSeconds":10,"successThreshold":1,"timeoutSeconds":10,"failureThreshold":60,"httpGet":{"host":"","path":"/checkpreload.htm","port":"80","scheme":"HTTP","httpHeaders":[]}}'
	   DEPLOY_CMD="${BASE_CMD_EDAS} DeployK8sApplication --AppId ${APP_ID} --JDK 'Open JDK 8'"


       //应用名称参数
       IMAGE_NAME = "amway-center"
       //edas 应用对应的ID
       EDAS_APPID = "${EDAS_APPID}"


       //K8S环境参数，正式环境可以先建立流行线变量${ALIYUN_ENV}
       //K8S_ENV = "${ALIYUN_ENV}"
       K8S_ENV = "${ALIYUN_ENV}"

       //获取Docker Registry的凭据信息
       //DOCKERREG_ACCESS_KEY = credentials('docker-registry-credentials')
       //定义镜像的Tag(即版本号由VersionNumber插件动态生成)
       //格式：<用于build的源码分支>-<build的所属年月日>-<当天的第N次build>
       //IMAGE_TAG = VersionNumber(projectStartDate: '1970-01-01', versionNumberString: '${BUILD_DATE_FORMATTED, "yyyyMMdd"}-${BUILDS_TODAY, XXX}', versionPrefix: "${K8S_ENV}-")
       IMAGE_TAG = "${K8S_ENV}-${env.BUILD_NUMBER}"
       //镜像中心的地址
       DOCKERREG_DOMAIN = "registry.cn-shanghai.aliyuncs.com"
       //镜像中心镜像所在的ns
       TARGET_NAMESPACE = "devops-cn-sh"
       //应用镜像的完整构成 @@TODO
       //APP_DOCKER_IMAGE ="${DOCKERREG_DOMAIN}/${TARGET_NAMESPACE}/${IMAGE_NAME}:${IMAGE_TAG}"
       APP_DOCKER_IMAGE="registry.cn-shanghai.aliyuncs.com/devops-cn-sh/aliyun-gts-user-center:legacy-53"

       //DOCKER
       DOCKER_USER = "@@TODO"
       DOCKER_PASSWORD = "@@TODO"
       //SonarQube平台的凭据信息
       //SONAR_QUBE_ACCESS_KEY = credentials('SONAR_QUBE_ADDR_TOKEN')
       DEFAULT_RECIPIENTS = "tanye.tyn@alibaba-inc.com"
    }
    stages {
        stage('Replace Environment Variables!'){
            steps {
                dir("${BASE_DIR}") {
                   //这句话，是查询所有前缀为application-dynamic的文件，查找里面所有的的APP_SQL_URI，替换成${APP_SQL_URI}的值
                   //如果是多环境的话，目前可以采用目前推荐的多环境支持方案https://yuque.antfin-inc.com/czi62s/rbznr4/hg17hs
                   //暂时注释，如果需要，自行使用
                   //sh "sed -i 's/APP_SQL_URI/${APP_SQL_URI}/g'  `find . -name application-dynamic.*`"
                   //sh "sed -i 's/APP_SQL_ACCOUNT/${APP_SQL_ACCOUNT}/g'  `find . -name application-dynamic.*`"
                   //sh "sed -i 's/APP_SQL_PASSWORD/${APP_SQL_PASSWORD}/g'  `find . -name application-dynamic.*`"
                   echo "step Replace Environment Variables!"
                }
            }
        }
        stage('Mvn Clean Compile Package Deploy!') {
			steps {
                dir("${BASE_DIR}") {
                    //代码 clean 和 compile,为代码检查等其他maven操作做准备
                    //sh 'mvn clean compile'
                    //mvn package
                    sh "mvn -U clean package -Dmaven.test.skip"
                    //mvn deploy
                    //sh "mvn -U deploy"、
					echo "step Mvn Clean Compile Package Deploy!"
                }
            }
        }
        stage('Parallel Stage') {
            parallel {
                stage('App\'s Code Sonar!') {
                    when {
                        //dev、test环境执行sonar检测
                        expression { K8S_ENV ==~ /(dev|test)/ }
                    }
                    steps {
                        dir("${BASE_DIR}") {
                            //安装代码覆盖率插件
                            //sh 'mvn org.jacoco:jacoco-maven-plugin:prepare-agent install -Dmaven.test.failure.ignore=true'
                            //发布到SonarQube平台
                            //sh 'mvn sonar:sonar -Dsonar.host.url=${SONAR_QUBE_ACCESS_KEY_USR} -Dsonar.login=${SONAR_QUBE_ACCESS_KEY_PSW} -T1C'
                            echo "step Code Sonar!"
						}
                    }
                }

                stage('Build App Image') {
                    steps {
                       dir("${BASE_DIR}") {
					        echo "setp Build App Image start!"
                            //以项目根目录为构建上下文 根据Dockerfile构建应用镜像
                            sh "docker build --no-cache -t ${APP_DOCKER_IMAGE} ."
                            //先登录到阿里云的Docker Registry
                            sh "docker login -u ${DOCKER_USER} -p ${DOCKER_PASSWORD} https://${DOCKERREG_DOMAIN}"
                            //将所构建的镜像推送到阿里云的Docker Registry
                            sh "docker push ${APP_DOCKER_IMAGE}"
                            // 删除WORK节点上保留的应用镜像
                            sh "docker rmi ${APP_DOCKER_IMAGE}"
							echo "setp Build App Image end!"
                       }
                    }
                }
            }
        }

        stage('Deploy App Image') {
            steps {
               dir("${BASE_DIR}") {
                  //CLI部署edas应用到集群 确保镜像包含aliyun cli
                  sh " aliyun edas POST /pop/v5/k8s/acs/k8s_apps --AppId ${APP_ID}  --Image ${APP_DOCKER_IMAGE} > ${env.WORKSPACE}/response: 2>&1"
                  //查看响应信息
                  sh "cat ${env.WORKSPACE}/response:"
                  //查看部署历史
                  sh " aliyun edas POST /pop/v5/changeorder/change_order_info --ChangeOrderId `sed -n 's/.*\"ChangeOrderId\": \"\\([a-z0-9-]*\\).*/\\1/p' ${env.WORKSPACE}/response:`"
			   }
            }
        }
    }
    //需要先配置全局邮件发送设置
    post {
        success {
            emailext (
                subject: "'${env.JOB_NAME} [${env.BUILD_NUMBER}]' 构建正常",
                body: """
                详情：<br/><hr/>
                &nbsp;&nbsp;<span style='color:green'>SUCCESSFUL: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]'</span><br/><hr/>
                &nbsp;&nbsp;项目名称：${env.JOB_NAME}<br/><hr/>
                &nbsp;&nbsp;构建编号：${env.BUILD_NUMBER}<br/><hr/>
                &nbsp;&nbsp;构建日志地址：<a href="${env.BUILD_URL}console">${env.BUILD_URL}console</a><br/><hr/>
                &nbsp;&nbsp;构建地址：<a href="${env.BUILD_URL}">${env.BUILD_URL}</a><br/><hr/>
                &nbsp;&nbsp;<b>本邮件是程序自动下发的，请勿回复！</b><br/><hr/>
                """,
                //邮箱可以替换成全局公告变量，需要自己定义
                to: "tanye.tyn@alibaba-inc.com",
                recipientProviders: [[$class: 'DevelopersRecipientProvider']]
             )
        }
        failure {
            emailext (
                subject: "'${env.JOB_NAME} [${env.BUILD_NUMBER}]' 构建失败",
                body: """
                详情：<br/><hr/>
                &nbsp;&nbsp;<span style='color:red'>FAILED: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]'</span><br/><hr/>
                &nbsp;&nbsp;项目名称：${env.JOB_NAME}<br/><hr/>
                &nbsp;&nbsp;构建编号：${env.BUILD_NUMBER}<br/><hr/>
                &nbsp;&nbsp;构建日志地址：<a href="${env.BUILD_URL}console">${env.BUILD_URL}console</a><br/><hr/>
                &nbsp;&nbsp;构建地址：<a href="${env.BUILD_URL}">${env.BUILD_URL}</a><br/><hr/>
                &nbsp;&nbsp;<b>本邮件是程序自动下发的，请勿回复！</b><br/><hr/>
                """,
                to: "tanye.tyn@alibaba-inc.com",
                recipientProviders: [[$class: 'DevelopersRecipientProvider']]
             )
        }
    }
}
