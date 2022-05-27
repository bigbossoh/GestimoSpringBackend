package com.bzdata.gestimospringbackend.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.EmptyInterceptor;
import org.slf4j.MDC;
import org.springframework.util.StringUtils;
@Slf4j
public class Interceptor extends EmptyInterceptor {

  @Override
  public String onPrepareStatement(String sql) {
    log.info("the sql request is {}", sql);
    if (StringUtils.hasLength(sql) && sql.toLowerCase().startsWith("select") && (MDC.get("idAgence")!=null)) {
      // select utilisateu0_.
      final String entityName = sql.substring(7, sql.indexOf("."));

      final String idEntreprise = MDC.get("idAgence");

      log.info("entityName {} and idEntreprise {}",entityName,idEntreprise);
      if (StringUtils.hasLength(entityName)
              &&
              !idEntreprise.toLowerCase().contains("1001")
          && !entityName.toLowerCase().contains("Agence")
          && !entityName.toLowerCase().contains("pays")
              && !entityName.toLowerCase().contains("ville")
              && !entityName.toLowerCase().contains("commune")
              && !entityName.toLowerCase().contains("role")
              && !entityName.toLowerCase().contains("quartier")
              && !entityName.toLowerCase().contains("site")
          && StringUtils.hasLength(idEntreprise))
      {

        if (sql.contains("where")) {
          sql = sql + " and " + entityName + ".id_agence = " + idEntreprise;
        } else {
          sql = sql + " where " + entityName + ".id_agence = " + idEntreprise;
        }
        log.info("on va append la clause where into the request {}",sql);
      }
      else {
        log.info("we are here");
        return super.onPrepareStatement(sql);
      }
    }
    return super.onPrepareStatement(sql);
  }
}
