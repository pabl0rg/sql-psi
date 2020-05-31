package com.alecstrong.sql.psi.core.psi.mixins

import com.alecstrong.sql.psi.core.psi.LazyQuery
import com.alecstrong.sql.psi.core.psi.NamedElement
import com.alecstrong.sql.psi.core.psi.SqlAlterTableStmt
import com.alecstrong.sql.psi.core.psi.SqlCompositeElementImpl
import com.alecstrong.sql.psi.core.psi.TableElement
import com.intellij.lang.ASTNode

internal abstract class AlterTableMixin(
  node: ASTNode
) : SqlCompositeElementImpl(node),
    SqlAlterTableStmt,
    TableElement {
  override fun name(): NamedElement = newTableName ?: tableName!!

  override fun tableExposed(): LazyQuery {
    return LazyQuery(
        tableName = name(),
        query = {
          (tableName!!.reference!!.resolve()!!.parent as TableElement)
              .tableExposed()
              .withAlterStatement(this)
              .query
        }
    )
  }
}
