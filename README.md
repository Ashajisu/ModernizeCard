# 서버정보
OCI AMD 인스턴스
- OCPU: 1
- RAM: 1GB
- Storage: 45GB
- SWAP: 4GB
- OS: Ubuntu 24.04 Minimal
- Application: HAB-springboot

## 주의사항
### 메모리 부족으로 인한 DB 연결 장애
- Backend와 MariaDB를 동일한 1GB AMD 인스턴스에서 운영할 경우 메모리 부족
- Backend에서 MariaDB 연결이 끊어질 수 있음
- Backend 재시작 후 DB 연결이 정상적으로 복구되는 현상 확인
- MariaDB 로그에서 `Memory pressure` 관련 메시지 확인
- 향후 Backend와 MariaDB를 별도 인스턴스로 분리하여 운영 필요

Sep 09 02:28:37 hab-back mariadbd[3695]: 2026-09-09  2:28:37 0 [Note] InnoDB: Memory pressure event disregarded; innodb_buffer_pool_size=128m,
 innodb_buffer_pool_size_auto_min=128m
 Caused by: org.hibernate.exception.SQLGrammarException: JDBC exception executing SQL [select u1_0.empno,u1_0.usertype,u1_0.deptcode,u1_0.name,u1_0.password,u1_0.position,u1_0.email from users u1_0 where u1_0.email=?] [(conn=30048) Unknown column 'u1_0.deptcode' in 'SELECT'] [n/a]
        at org.hibernate.exception.internal.SQLExceptionTypeDelegate.convert(SQLExceptionTypeDelegate.java:66)

## 명령
ssh -v -i <SSH_KEY> ubuntu@<SERVER_IP>
systemctl status backend
systemctl status mariadb

ss -lntp | grep 9090
ss -lntp | grep 3306

cd /var/log/backend/
view application.log

mariadb -u <DB_USER> -p
show databases;
use <DB_NAME>;
show tables;

## 콘솔기록
ubuntu    195794       1  0 Aug13 ?        01:02:16 /usr/bin/java -Xmx600m -jar /opt/backend/modernize
    PID     ELAPSED %CPU %MEM   RSS CMD
 195794 27-21:50:33  0.1 19.2 187832 /usr/bin/java -Xmx600m -jar /opt/backend/modernize
ubuntu@hab-back:/var/log/backend$ ss -lntp | grep 9090
LISTEN 0      100                *:9090            *:*    users:(("java",pid=195794,fd=20))
ubuntu@hab-back:/var/log/backend$ df -h
Filesystem      Size  Used Avail Use% Mounted on
tmpfs            96M  1.1M   95M   2% /run
/dev/sda1        45G  7.5G   37G  17% /
tmpfs           478M     0  478M   0% /dev/shm
tmpfs           5.0M     0  5.0M   0% /run/lock
efivarfs        256K   17K  235K   7% /sys/firmware/efi/efivars
/dev/sda16      881M  165M  654M  21% /boot
/dev/sda15      105M  6.2M   99M   6% /boot/efi
tmpfs            96M   12K   96M   1% /run/user/1001
