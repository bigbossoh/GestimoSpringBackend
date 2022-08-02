package com.bzdata.gestimospringbackend.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.EmptyInterceptor;
import org.slf4j.MDC;
import org.springframework.context.annotation.Bean;
import org.springframework.util.StringUtils;
@Slf4j
public class Interceptor extends EmptyInterceptor {

  @Override
  @Bean
  public String onPrepareStatement(String sql) {
    log.info("the sql incoming request is : {}", sql);
    if (StringUtils.hasLength(sql) && sql.toLowerCase().startsWith("select") && (MDC.get("idAgence")!=null)) {
      // select utilisateu0_.
      final String entityName = sql.substring(7, sql.indexOf("."));

      final String idAgence = MDC.get("idAgence");

      log.info("entityName {} and idAgence {}",entityName,idAgence);
      if (StringUtils.hasLength(entityName)
              &&
              !idAgence.toLowerCase().contains("1001")
          //    && !entityName.toLowerCase().contains("utilisateur")
                  && !entityName.toLowerCase().contains("operation")
              && !entityName.toLowerCase().contains("bail")
              && !entityName.toLowerCase().contains("Agence")
          && !entityName.toLowerCase().contains("count")
              && !entityName.toLowerCase().contains("coalesce")
              && !entityName.toLowerCase().contains("pays")
              && !entityName.toLowerCase().contains("ville0_")
              && !entityName.toLowerCase().contains("commune")
              && !entityName.toLowerCase().contains("role")
              && !entityName.toLowerCase().contains("quartier")
          && !entityName.toLowerCase().contains("site")
          && !entityName.toLowerCase().contains("studio")
          && !entityName.toLowerCase().contains("appartemen")
          && StringUtils.hasLength(idAgence))
      {

        if (sql.contains("where")) {
          sql = sql + " and " + entityName + ".id_agence = " + idAgence;
        } else {
          sql = sql + " where " + entityName + ".id_agence = " + idAgence;
        }
        log.info("on va append la clause where into the request {}",sql);
      }
      else {
        log.info("We are not apped the where clause {}",sql);
        return super.onPrepareStatement(sql);
      }
    }
    return super.onPrepareStatement(sql);
  }
}
