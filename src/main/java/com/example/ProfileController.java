package com.example;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Map;

//auth0 login imports
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import org.springframework.web.bind.annotation.GetMapping;

@Controller
@SpringBootApplication
public class ProfileController {
    @Value("${spring.datasource.url}")
    private String dbUrl;
  
    @Autowired
    private DataSource dataSource;
    
    @GetMapping("/ManageUsers")
    public String ManageUsers(Map<String, Object> model, @AuthenticationPrincipal OidcUser principal)
    {
        GetuserAuthenticationData(model,principal);
        try (Connection connection = dataSource.getConnection()) 
        {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(("SELECT * FROM users"));
            ArrayList<PermissionData> dataList = new ArrayList<PermissionData>();
            
            while (rs.next()) {
                PermissionData obj = new PermissionData();
                obj.setEmail(rs.getString("email"));
                obj.setRole(rs.getString("role"));        
                dataList.add(obj);
            }
            model.put("Users", dataList);
            return "ProfileController";
        }
        catch (Exception e) {
            model.put("message", e.getMessage());
            return "error";
        }
    }
    @PostMapping(path = "/ManageUsers")
    public String handleUserElementSubmit(Map<String, Object> model,PermissionData permissiondata,@AuthenticationPrincipal OidcUser principal) throws Exception
    {
        GetuserAuthenticationData(model,principal);
        try (Connection connection = dataSource.getConnection()) {
        Statement stmt = connection.createStatement();
        String sql = "UPDATE users set role = '"+ permissiondata.getRole() +"' WHERE email = '"+permissiondata.getEmail()+"';";
        System.out.println(sql);
        stmt.executeUpdate(sql);
        ResultSet rs = stmt.executeQuery(("SELECT * FROM users"));
        ArrayList<PermissionData> dataList = new ArrayList<PermissionData>();
          
        while (rs.next()) {
            PermissionData obj = new PermissionData();
            obj.setEmail(rs.getString("email"));
            obj.setRole(rs.getString("role"));        
            dataList.add(obj);
        }
        model.put("Users", dataList);
        return "ProfileController";
        }
        catch (Exception e) {
            model.put("message", e.getMessage());
            return "error";
        }
    }
    
    public String GetuserAuthenticationData(Map<String, Object> model,@AuthenticationPrincipal OidcUser principal)
  {
    String defaultrole = "unverified";
    if (principal != null) 
    {
      model.put("profile", principal.getClaims());
      String email = (String) principal.getClaims().get("email");
      System.out.println(email);
      try (Connection connection = dataSource.getConnection()) 
      {
        Statement stmt = connection.createStatement();
        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS users (email varchar(50),role varchar(10));");
        ResultSet rs = stmt.executeQuery(("SELECT * FROM users WHERE email='"+email+"'"));
        if(rs.next())
        {
          model.put("userRole",rs.getString("role"));
          return rs.getString("role");
        }
        else
        {
          stmt.executeUpdate("INSERT INTO users (email,role) VALUES ('"+email+"','"+defaultrole+"');");
          model.put("userRole",defaultrole);
          return defaultrole;
        }
      } 
      catch (Exception e) 
      {
        model.put("message", e.getMessage());
        System.out.println(e);
        return "error";
      }
    }
    else
    {
      model.put("userRole","Not Logged In");
      return "Not Logged In";
    }
    //Database calls for the role in question
  }
}