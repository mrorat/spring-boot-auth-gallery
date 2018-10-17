//package com.quasar.repository;
//
//import javax.persistence.Column;
//import javax.persistence.ColumnResult;
//import javax.persistence.Entity;
//import javax.persistence.Id;
//import javax.persistence.NamedNativeQuery;
//import javax.persistence.NamedQuery;
//import javax.persistence.SqlResultSetMapping;
//
//@Entity
//@NamedNativeQuery(
//					name = "getAlbumsForUser", 
//					query = "call getAlbumsForUser(?)", 
//					resultSetMapping = "mapping", 
//					resultClass = NamedQuery.class)
//@SqlResultSetMapping(name = "mapping", columns = @ColumnResult(name = "value"))
//public class NamedQueryGetAlbumsForUser {
//
//	@Id
//	public String userId;
//}