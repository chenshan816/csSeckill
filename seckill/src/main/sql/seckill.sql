DELIMITER $$ --;转换为$$

--定义存储过程
--row_count():返回上一条修改类型sql影响的行数
--row_count()返回值：<0 没有修改的sql语句，=0未对数据库进行修改，>0对数据库进行修改
CREATE PROCEDURE `seckill`.`execute_seckill`
			(in v_seckill_id bigint,in v_phone bigint
			 in v_kill_time timestamp,out r_result int)
BEGIN
	DECLARE insert_count int DEFAULT 0;
	START TRANSACTION;
	insert ignore into success_killed
		(seckill_id,user_phone,create_time)
		values (v_seckill_id,v_phone,v_kill_time)
		select row_count() into insert_count;
		IF (insert_count ==0) THEN
			ROLLBACK;
			set r_result =-1;
		ELSEIF(insert_count <0) THEN
			ROLLBACK;
			set r_result =-2;
		ELSE
			update 
    		seckill
    		set number = number -1
    		where seckill_id = v_seckill_id
    		and start_time <= v_kill_time
    		and end_time >= v_kill_time
    		and number >0;
    		select row_count() into insert_count;
    		IF(insert_count ==0) THEN
    			ROLLBACK;
    			set r_result =0;
    		ELSEIF(insert_count <0) THEN
				ROLLBACK;
				set r_result =-2;
			ELSE
				COMMIT;
				set r_result = 1;
			END IF;
		END IF;
	END;
$$