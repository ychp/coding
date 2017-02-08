-- 创建表
create table if not exists terminus.dwd_scrm_casarte_hcustomertype(
horus_id				string,
)
PARTITIONED BY (
  pt string)
STORED AS PARQUET
TBLPROPERTIES (
  'parquet.compression'='SNAPPY'
) ;

-- 增量插入
Insert overwrite table terminus.dwd_scrm_casarte_hcustomertype partition(pt='${PT_DATE}')
select
    om.horus_id,
from (
	SELECT
	pm.horus_id,
	row_number() over (partition by pm.horus_id order by pm.dti_update_dt desc) num
	FROM (
		select
		    case when b.horus_id is null then reflect("java.util.UUID", "randomUUID") else b.horus_id end as horus_id,
			am.dti_updateuid
		from (
				select
					t.dti_updateuid
				from (
					select
						dti_updateuid,
						row_number() over (partition by dti_id order by dti_update_dt desc) num
					from terminus.s_scrm_casarte_hcustomertype where pt='${PT_DATE}'
				) t
				where t.num=1
			) am  left join ( select  horus_id, dti_id, dti_update_dt from terminus.dwd_scrm_casarte_hcustomertype where pt='${YPT_DATE}' ) b  on am.dti_id = b.dti_id
		union all
		select
			horus_id,
			dti_updateuid
		from terminus.dwd_scrm_casarte_hcustomertype where pt = '${YPT_DATE}'
	)  pm
) om where om.num=1 ;