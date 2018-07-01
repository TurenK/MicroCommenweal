<?php
	require_once('./db.php');
	require_once('./response.php');
	$conn = Db::connect();

	$sql = "select question,tof
	from questiontof
	order by rand()
	limit 5";
	
	
	$resultset = array();
	mysqli_real_query($conn, $sql);
	if ($result = mysqli_store_result($conn)) {
		$i = 0;			
		while ($row = mysqli_fetch_row($result)) {
			$i++;
			$eachrow = array(
				'question' => $row[0],
				'tof' => $row[1]
			);
			$resultset[$i] = $eachrow;
		}
		mysqli_free_result($result);
	}
	
	if ($resultset) {
		return response::show(200,'true',$resultset);
	} else {
		return response::show(400,'false','data deliver fail');
	}

	$conn->close();
?> 