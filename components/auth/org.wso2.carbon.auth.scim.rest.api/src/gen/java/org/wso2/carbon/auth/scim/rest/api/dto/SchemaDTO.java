package org.wso2.carbon.auth.scim.rest.api.dto;


import java.util.Objects;

/**
 * SchemaDTO
 */
public class SchemaDTO   {

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    return Objects.hash();
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SchemaDTO {\n");
    
    sb.append("}");
    return sb.toString();
  }

}

