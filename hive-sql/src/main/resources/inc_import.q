-- 创建表
create table if not exists ${database}.dwd_${table_name}(
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
    om.horus_id,
    om.${columns}
from (
	SELECT
    pm.horus_id,
    pm.${columnsWithSuf}
    row_number() over (partition by pm.horus_id order by pm.${inc_column} desc) num
	FROM (
		select
		    case when b.horus_id is null then reflect("java.util.UUID", "randomUUID") else b.horus_id end as horus_id,
			  am.${columns}
		from (
				select
					t.${columns}
				from (
					select
						${columnsWithSuf}
					from ${database}.s_${table_name} where pt='${PT_DATE}'
				) t
			) am  left join
			( select
			  horus_id,
			  ${left_join_columns},
			  ${inc_column}
			from ${database}.dwd_${table_name} where pt='${YPT_DATE}' ) b
			on
			am.${left_join_columns} = b.${left_join_columns}
		union all
		select
			horus_id,
			${columns}
		from ${database}.dwd_${table_name} where pt = '${YPT_DATE}'
	)  pm
) om where om.num=1 ;