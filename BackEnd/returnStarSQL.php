<?php
	require_once('./db.php');
	require_once('./response.php');
	class Recommand{
		public static function getListinfo( $connect, $Id, $num ){	
			if($num == 1){
				$sql = "select 
					userId,activity.activityId,activitySponsor,activityType,activityName,activityImage,
					activityReleaseTime,activityDeadline,activityStartTime,activityEndTime,
					activityAddress,activityTel,activityIntroduction,aNeedNumOfPerson,aSurplusQuota,
					activityStatus
					from activity,staruser
					where activity.activityId = staruser.activityId and userId = '".$Id."' ";
			}else if($num == 2){
//				return response::show(200,'true',$Id);
				$sql = "select 
					groupId,activity.activityId,activitySponsor,activityType,activityName,activityImage,
					activityReleaseTime,activityDeadline,activityStartTime,activityEndTime,
					activityAddress,activityTel,activityIntroduction,aNeedNumOfPerson,aSurplusQuota,
					activityStatus
					from activity,stargroup
					where activity.activityId = stargroup.activityId and groupId = '".$Id."' ";
			}
			
			$resultset = array();
			mysqli_real_query($connect, $sql);
//			return response::show(200,'true',$sql);
			if ($result = mysqli_store_result($connect)) {
//				return response::show(200,'true','666');
				$i = 0;			
				while ($row = mysqli_fetch_row($result)) {
//					return response::show(200,'true','777');
					$i++;
					$eachrow = array(
						'activityId' => $row[1],
						'activitySponsor' => $row[2],
						'activityType' => $row[3],
						'activityName' => $row[4],
						'activityImage' => $row[5],
						'activityReleaseTime' => $row[6],
						'activityDeadline' => $row[7],
						'activityStartTime' => $row[8],
						'activityEndTime' => $row[9],
						'activityAddress' => $row[10],
						'activityTel' => $row[11],
						'activityIntroduction' => $row[12],
						'aNeedNumOfPerson' => $row[13],
						'aSurplusQuota' => $row[14],
						'activityStatus' => $row[15]
					);
//					return response::show(200,'true',$eachrow);
					$resultset[$i] = $eachrow;
				}
				mysqli_free_result($result);
			}
			$resultset['id'] = $Id;
			return $resultset;
		}
	}
?>