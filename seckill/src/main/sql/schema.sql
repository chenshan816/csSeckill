--���ݿ��ʼ���ű�
--�������ݿ�
CREATE DATABASE seckill;
--ʹ�����ݿ�
use seckill;CREATE TABLE seckill(
`seckill_id` bigint NOT NULL AUTO_INCREMENT COMMENT '��Ʒ���id',
`name` varchar(120) NOT NULL COMMENT '��Ʒ����',
`number` int NOT NULL COMMENT '�������',
`start_time` timestamp NOT NULL COMMENT '��ɱ����ʱ��',
`end_time` timestamp NOT NULL COMMENT '��ɱ����ʱ��',
`create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '����ʱ��',
PRIMARY KEY (seckill_id),
key idx_start_time(start_time),
key idx_end_time(end_time),
key idx_create_time(create_time)
)ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8 COMMENT='��ɱ����';
--������ɱ����


--��ʼ������
insert into seckill(name,number,start_time,end_time)
values ('��ɱiphone6s',100,'2017-1-17 00:00:00','2017-2-17 00:00:00'),
('��ɱipad2',200,'2017-1-20 00:00:00','2017-2-17 00:00:00'),
('��ɱС��4',300,'2017-1-17 00:00:00','2017-2-17 00:00:00'),
('��ɱ����',400,'2017-1-17 00:00:00','2017-2-17 00:00:00');

--��ɱ�ɹ���ϸ��
--�û���¼��֤�����Ϣ
CREATE TABLE success_killed(
`seckill_id` bigint NOT NULL COMMENT '��ɱ��ƷID',
`user_phone` bigint NOT NULL COMMENT '�û��ֻ���',
`state` tinyint NOT NULL DEFAULT -1 COMMENT '״̬��ʾ->-1:��Ч 0���ɹ��µ���δ����1���Ѹ���',
`create_time` timestamp NOT NULL COMMENT '����ʱ��',
PRIMARY KEY(seckill_id,user_phone),/*��������*/
key idx_create_time(create_time)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='��ɱ�ɹ���ϸ��';





