<?php
header("Content-type:text/html;charset=utf8");
	require_once('./db.php');
	class history{
		public static function getListinfo($id, $pagesize, $connect, $which){
			if($which == "0"){
				//组织发起的活动
				$sql = "select activity.activityId,activity.activityName,activity.activityImage,activity.aSurplusQuota
				,activity.activityStatus 
				from activity 
				where activitySponsor =".$id." 
				group by activity.activityId 
				order by activityReleaseTime 
				limit " . $pagesize;
			}else if($which == "1"){
				//用户参加的活动
				$sql = "select activity.activityId,activity.activityName,activity.activityImage,activity.aSurplusQuota
				,activity.activityStatus 
				from activity, participate 
				where participate.userId =".$id." and participate.activityId = activity.activityId 
				group by activity.activityId 
				order by activityReleaseTime 
				limit " . $pagesize;
			}else{
				$sql = "select activityId,activityName,activityImage,aSurplusQuota
				,activityStatus 
				from activity 
				where activitySponsor =".$id." 
				group by activity.activityId 
				order by activityReleaseTime 
				limit " . $pagesize;
			}
			
			$resultset = array();
			mysqli_real_query($connect, $sql);
			if ($result = mysqli_store_result($connect)) {
				$i = 0;
				while ($row = mysqli_fetch_row($result)) {
					$i++;
					$eachrow = array(
						'activityId' => $row[0],
						'activityName' => $row[1],
						'activityImage' => $row[2],
						'aSurplusQuota' => $row[3],
						'activityStatus' => $row[4]
					);
					$resultset[$i] = $eachrow;
				}
				mysqli_free_result($result);
			}
			return $resultset;
		}
	}

				