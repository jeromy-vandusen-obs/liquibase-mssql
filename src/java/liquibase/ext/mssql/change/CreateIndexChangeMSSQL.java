package liquibase.ext.mssql.change;

import liquibase.change.ChangeMetaData;
import liquibase.change.DatabaseChange;
import liquibase.change.core.CreateIndexChange;
import liquibase.database.Database;
import liquibase.ext.mssql.database.MSSQLDatabase;
import liquibase.ext.mssql.statement.CreateIndexStatementMSSQL;
import liquibase.statement.SqlStatement;
import liquibase.statement.core.CreateIndexStatement;

import java.util.ArrayList;
import java.util.List;

@DatabaseChange(name = "createIndex", description = "Creates an index on an existing column or set of columns.", priority = ChangeMetaData.PRIORITY_DATABASE, appliesTo = "index")
public class CreateIndexChangeMSSQL extends CreateIndexChange {
  private String includedColumns;

  private Integer fillFactor;

  public String getIncludedColumns() {
    return includedColumns;
  }

  public void setIncludedColumns(String includedColumns) {
    this.includedColumns = includedColumns;
  }

  public Integer getFillFactor() { return fillFactor; }

  public void setFillFactor(Integer fillFactor) { this.fillFactor = fillFactor; }

  @Override
  public SqlStatement[] generateStatements(Database database) {
    SqlStatement[] statements = super.generateStatements(database);
    if (!MSSQLDatabase.PRODUCT_NAME.equals(database.getDatabaseProductName())) {
    	return statements;
    }

    List<SqlStatement> extendedStatements = new ArrayList<SqlStatement>(statements.length);
    for (SqlStatement statement : statements) {
      if (statement instanceof CreateIndexStatement) {
        extendedStatements.add(new CreateIndexStatementMSSQL((CreateIndexStatement)statement, includedColumns, fillFactor));
      } else {
        extendedStatements.add(statement);
      }
    }

    return extendedStatements.toArray(new SqlStatement[0]);
  }

  @Override
  public boolean supports(Database database) {
    return database instanceof MSSQLDatabase;
  }
}
