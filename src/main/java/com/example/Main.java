/*
 * Copyright 2002-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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

import jdk.jfr.Registered;

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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;



@Controller
@SpringBootApplication
public class Main {

  @Value("${spring.datasource.url}")
  private String dbUrl;

  @Autowired
  private DataSource dataSource;

  public static void main(String[] args) throws Exception {
    SpringApplication.run(Main.class, args);
  }

  @RequestMapping("/")
  String index(Map<String, Object> model, @AuthenticationPrincipal OidcUser principal) {
    if (principal != null) {model.put("profile", principal.getClaims());}
    try (Connection connection = dataSource.getConnection()) {
      Statement stmt = connection.createStatement();
      // stmt.executeUpdate("CREATE TABLE IF NOT EXISTS squares (id serial, boxname
      // varchar(20), height int, width int, boxcolor char(7), outlined boolean)");
      return "index";
    } catch (Exception e) {
      model.put("message", e.getMessage());
      return "error";
    }
  }

  @GetMapping("/WorkItemSubmit")
  String LoadFormWorkItem(Map<String, Object> model) {
    WorkItem workitem = new WorkItem();
    model.put("WorkItem", workitem);
    return "WorkItemSubmit";
  }

  @GetMapping("/PositionSubmit")
  String LoadFormPosition(Map<String, Object> model) {
    Position position = new Position();
    model.put("Position", position);
    return "PositionSubmit";
  }

  //Submit Catch
  @PostMapping(
    path = "/WorkItemSubmit",
    consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE}
  )
  public String handleBrowsernewWorkItemSubmit(Map<String, Object> model, WorkItem workitem) throws Exception {
    try (Connection connection = dataSource.getConnection()) {
      Statement stmt = connection.createStatement();
      //stmt.executeUpdate("CREATE TABLE IF NOT EXISTS workitems (id serial, itemname varchar(20), startdate char(10), enddate char(10), teams [],itemtype varchar(3), fundinginformation varchar(100))");
      String sql = "INSERT INTO workitems (name, startdate, enddate, itemtype, fundinginformation) VALUES ('"+workitem.getItemName()+ "', '"+workitem.getStartDate()+ "', '"+ "'"+workitem.getEndDate()+ "', '"+workitem.getItemType()+ "', '"+workitem.getFundingInformation()+ "');";
      //stmt.executeUpdate(sql);
      return "redirect:/";
    } catch (Exception e) {
      model.put("message", e.getMessage());
      return "error";
    }
  }

  @Bean
  public DataSource dataSource() throws SQLException {
    if (dbUrl == null || dbUrl.isEmpty()) {
      return new HikariDataSource();
    } else {
      HikariConfig config = new HikariConfig();
      config.setJdbcUrl(dbUrl);
      return new HikariDataSource(config);
    }
  }

}
