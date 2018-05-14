<?php
	require_once('./db.php');
	class history{
		public static function getListinfo($userid, $pagesize, $connect){
			$sql = "select activityName,activityImage,aSurplusQuota,activityStatus from activity where activitySponsor = $userid order by activityReleaseTime limit " . $pagesize;
			$resultset = array();
			mysqli_real_query($connect, $sql);
			if ($result = mysqli_store_result($connect)) {
				while ($row = mysqli_fetch_row($result)) {
					$eachrow = array(
						'activityName' => $row[0],
						'activityImage' => $row[1],
						'aSurplusQuota' => $row[2],
						'activityStatus' => $row[3]
					);
					$resultset[] = $eachrow;
				}
				mysqli_free_result($result);
			}
			return $resultset;
		}
	}
