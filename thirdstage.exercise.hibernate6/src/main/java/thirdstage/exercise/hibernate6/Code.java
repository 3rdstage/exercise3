package thirdstage.exercise.hibernate6;

import org.hibernate.annotations.Comment;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Code {

  @Id
  @Column(length = 30)
  @Comment("code")
  private String code;

  @Column(length = 200, nullable = false)
  @Comment("name")
  private String name;

  @Column(name = "descr", nullable = true)
  @Comment("description")
  private String description;

  @Override
	protected String columnType(int sqlTypeCode) {
		switch ( sqlTypeCode ) {
			case TINYINT:
				// no tinyint, not even in Postgres 11
				return "smallint";

			// there are no nchar/nvarchar types in Postgres
			case NCHAR:
				return columnType( CHAR );
			case NVARCHAR:
				return columnType( VARCHAR );

			// since there's no real difference between TEXT and VARCHAR,
			// except for the length limit, we can just use 'text' for the
			// "long" string types
			case LONG32VARCHAR:
			case LONG32NVARCHAR:
				return "text";

			case BLOB:
			case CLOB:
			case NCLOB:
				// use oid as the blob/clob type on Postgres because
				// the JDBC driver doesn't allow using bytea/text via
				// LOB APIs
				return "oid";

			// use bytea as the "long" binary type (that there is no
			// real VARBINARY type in Postgres, so we always use this)
			case BINARY:
			case VARBINARY:
			case LONG32VARBINARY:
				return "bytea";

			// We do not use the time with timezone type because PG deprecated it and it lacks certain operations like subtraction
//			case TIME_UTC:
//				return columnType( TIME_WITH_TIMEZONE );

			case TIMESTAMP_UTC:
				return columnType( TIMESTAMP_WITH_TIMEZONE );

			default:
				return super.columnType( sqlTypeCode );
		}
	}


}
