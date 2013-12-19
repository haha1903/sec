package com.datayes.pms.sec

trait LookupStrategy {
  def readAclsById(objects: List[ObjectIdentity], sids: List[Sid]): Map[ObjectIdentity, Acl]
}
