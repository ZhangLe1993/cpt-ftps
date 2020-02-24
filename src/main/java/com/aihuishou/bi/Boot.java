package com.aihuishou.bi;

import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.ftplet.Authority;
import org.apache.ftpserver.listener.ListenerFactory;
import org.apache.ftpserver.usermanager.impl.BaseUser;
import org.apache.ftpserver.usermanager.impl.WritePermission;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class Boot implements CommandLineRunner {
    @Value("${ftp.port}")
    private Integer port;

    @Value("${ftp.username}")
    private String username;

    @Value("${ftp.password}")
    private String password;

    @Value("${ftp.dir}")
    private String dir;

    @Override
    public void run(String... args) throws Exception {
        FtpServerFactory serverFactory = new FtpServerFactory();
        ListenerFactory factory = new ListenerFactory();
        //设置监听端口
        factory.setPort(port);
        //替换默认监听
        serverFactory.addListener("default", factory.createListener());
        //用户名
        BaseUser user = new BaseUser();
        user.setName(username);
        //密码 如果不设置密码就是匿名用户
        user.setPassword(password);
        //用户主目录
        user.setHomeDirectory(dir);
        List<Authority> authorities = new ArrayList<>();
        //增加写权限
        authorities.add(new WritePermission());
        user.setAuthorities(authorities);
        //增加该用户
        serverFactory.getUserManager().save(user);
        FtpServer server = serverFactory.createServer();
        server.start();
    }
}
