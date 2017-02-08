-- 创建表
create table if not exists ${database}.dwd_${table_name} (
horus_id				string,
${table_columns}
)
PARTITIONED BY (
  pt string)
STORED AS PARQUET
TBLPROPERTIES (
  'parquet.compression'='SNAPPY'
) ;

-- 增量插入
Insert overwrite table ${database}.dwd_${table_name} partition(pt='${PT_DATE}')
select
	reflect("java.util.UUID", "randomUUID"),
	${columns}
from ${database}.s_${table_name};