<?php
	header("Content-type:text/html;charset=utf8");
	require_once('./db.php');
	require_once('./response.php');
	require_once('./history.php');
	$connect = db::connect();

	$json = file_get_contents('php://input');
	$arr = json_decode($json, true);
	$gId = $arr['gId'];

	//$gId = 3;//测试

	$sql = "select distinct activity.activityId,activity.activityName,activity.activityImage,activity.aSurplusQuota,activity.aNeedNumOfPerson 
				,activity.activityStatus from activity 
				left join organization on (activity.activityId=organization.aId and activity.activitySponsor=organization.gId) 
				where organization.uId is null activity.activitySponsor=6 
				and activity.activityStatus = '0'
				and activity.aNeedNumOfPerson > activity.aSurplusQuota
				group by activity.activityId 
				order by activityReleaseTime";
				$resultset = array();
	mysqli_real_query($connect, $sql);
			
	if ($result = mysqli_store_result($connect)) {
		$i = 0;			
		while ($row = mysqli_fetch_row($result)) {
			$i++;
			$userStatus=0;
			$eachrow = array(
			'activityId' => $row[0],
			'activityName' => $row[1],
			'activityImage' => $row[2],
			'aSurplusQuota' => $row[3],
			'aNeedNumOfPerson' => $row[4],
			'activityStatus' => $row[5]
			
			);
			$resultset[$i] = $eachrow;		
		}
		mysqli_free_result($result);
	}
	if($resultset) {
		return Response::show(200, 'true', $resultset);
	} else if(mysqli_num_rows($resultset) == 0){
		return Response::show(203, 'true', 'no activity need to evaluated');
	}else{
		return Response::show(400, 'false', 'data deliver fail');
	}
?>