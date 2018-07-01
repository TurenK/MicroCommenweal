<?php
header("Content-type:text/html;charset=utf8");
	require_once('./db.php');
	require_once('./response.php');
	$conn = Db::connect();
	
	//返回组织发起过的活动数量
	
	$json = file_get_contents('php://input');
	$arr = json_decode($json, true);

	$activityId = $arr['actId'];
	//test
	//$activityId = 1;
//	echo $activityId ;
	/*
	actname–string—活动名
	actimage—string—活动头像地址
	actid–string–活动的id
	actcom–int–活动的综合评分(暂时定义为平均分)
	actdesc—string—活动简介
	
	 (list, 每个item前以数字增序作为key): 
	userid–string—用户id
	username–string–用户名
	userimage—string—用户头像url
	usercom—int—用户评价等级
	usercomdesc—string—用户评价描述

	*/
	
	$sql1 = "select activityId,activityName,activityImage,activityIntroduction
	from activity
	where activityId = '".$activityId."' ";
	$sql2 = "select avg(ratnum)
	from evaluateusertogroup
	where activityId = '".$activityId."' ";
	$sql3 = "select evaluateusertogroup.userId,userName,userImage,ratnum,cominfo
	from evaluateusertogroup,user
	where activityId = '".$activityId."' and evaluateusertogroup.userId = user.userId";

	$result1 = mysqli_query ( $conn, $sql1 );
	
	if(mysqli_num_rows($result1) ==1){
		while($row = mysqli_fetch_row($result1)) {
			$rows=array(
				'actid'=>$row[0],
				'actname'=>$row[1],
				'actimage'=>$row[2],
				'actdesc'=>$row[3],
			);
		} 
//		return Response::show(200,'true',$rows);
		$result2 = mysqli_query ( $conn, $sql2 );
		if($row = mysqli_fetch_row($result2)){
			$rows['actcom'] = (int)$row[0];
//			return Response::show(200,'true',$rows);
			
			$result3 = mysqli_query ( $conn, $sql3 );
			if(mysqli_num_rows($result3) >=1){
				$i=0;
				while ( $row = mysqli_fetch_row($result3)) {
					$i++;
					$eachrow=array(
						'userid'=>$row[0],
						'username'=>$row[1],
						'userimage'=>$row[2],
						'usercom'=>$row[3],
						'usercomdesc'=>$row[4]
					);
					$rows[$i] = $eachrow;
				}
				return Response::show(200,'true',$rows);
			}else{
				return Response::show(401, 'false','get user information wrong');
			}
		}else{
			return Response::show(401, 'false','get actcom wrong');
		}
		
	}else if(mysqli_num_rows($result1) > 1){
		return Response::show(401, 'false','false,data:illegal data');
	}

	$conn->close();
?> 