package com.datayes.pms.sec

object BasicLookupStrategy extends LookupStrategy {
  private var selectClause = "select acl_object_identity.object_id_identity, " + "acl_entry.ace_order,  " + "acl_object_identity.id as acl_id, " + "acl_object_identity.parent_object, " + "acl_object_identity.entries_inheriting, " + "acl_entry.id as ace_id, " + "acl_entry.mask,  " + "acl_entry.granting,  " + "acl_entry.audit_success, " + "acl_entry.audit_failure,  " + "acl_sid.principal as ace_principal, " + "acl_sid.sid as ace_sid,  " + "acli_sid.principal as acl_principal, " + "acli_sid.sid as acl_sid, " + "acl_class.class " + "from acl_object_identity " + "left join acl_sid acli_sid on acli_sid.id = acl_object_identity.owner_sid " + "left join acl_class on acl_class.id = acl_object_identity.object_id_class   " + "left join acl_entry on acl_object_identity.id = acl_entry.acl_object_identity " + "left join acl_sid on acl_entry.sid = acl_sid.id  " + "where ( "
  private var lookupPrimaryKeysWhereClause = "(acl_object_identity.id = ?)"
  private var lookupObjectIdentitiesWhereClause = "(acl_object_identity.object_id_identity = ? and acl_class.class = ?)"
  private var orderByClause = ") order by acl_object_identity.object_id_identity" + " asc, acl_entry.ace_order asc"
  def readAclsById(objects: List[ObjectIdentity], sids: List[Sid]): Map[ObjectIdentity, Acl] = {
    objects.map { identity =>
      val acl = lookupObjectIdentitiy(identity, sids)
      (identity, acl)
    }.filter(_._2 != null).toMap
  }
  def lookupObjectIdentitiy(identity: ObjectIdentity, sids: List[Sid]): Acl = {
    null
  }
}
