package com.datayes.pms.sec

import com.datayes.pms.sec.model.NotFoundException
import javax.sql.DataSource

object AclService {
  //~ Instance fields ================================================================================================
  private var findChildrenSql = "select obj.object_id_identity as obj_id, class.class as class " + "from acl_object_identity obj, acl_object_identity parent, acl_class class " + "where obj.parent_object = parent.id and obj.object_id_class = class.id " + "and parent.object_id_identity = ? and parent.object_id_class = (" + "select id FROM acl_class where acl_class.class = ?)"

  private var foreignKeysInDatabase: Boolean = true
  private var deleteEntryByObjectIdentityForeignKey = "delete from acl_entry where acl_object_identity=?"
  private var deleteObjectIdentityByPrimaryKey = "delete from acl_object_identity where id=?"
  private var classIdentityQuery = "call identity()"
  private var sidIdentityQuery = "call identity()"
  private var insertClass = "insert into acl_class (class) values (?)"
  private var insertEntry = "insert into acl_entry " + "(acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure)" + "values (?, ?, ?, ?, ?, ?, ?)"
  private var insertObjectIdentity = "insert into acl_object_identity " + "(object_id_class, object_id_identity, owner_sid, entries_inheriting) " + "values (?, ?, ?, ?)"
  private var insertSid = "insert into acl_sid (principal, sid) values (?, ?)"
  private var selectClassPrimaryKey = "select id from acl_class where class=?"
  private var selectObjectIdentityPrimaryKey = "select acl_object_identity.id from acl_object_identity, acl_class " + "where acl_object_identity.object_id_class = acl_class.id and acl_class.class=? " + "and acl_object_identity.object_id_identity = ?"
  private var selectSidPrimaryKey = "select id from acl_sid where principal=? and sid=?"
  private var updateObjectIdentity = "update acl_object_identity set " + "parent_object = ?, owner_sid = ?, entries_inheriting = ?" + " where id = ?"
  def readAclsById(objects: List[ObjectIdentity], sids: List[Sid])(implicit ds: DataSource, lookupStrategy: LookupStrategy): Map[ObjectIdentity, Acl] = {
    val result = lookupStrategy.readAclsById(objects, sids)
    objects.foreach(oid =>
      if (!result.contains(oid)) throw new NotFoundException("Unable to find ACL information for object identity '" + oid + "'")
    )
    result
  }
}
