<?php
require_once('DB_Connect.php');
 $dbobj = new dbconnect();
 $con = $dbobj->connect();
 
 if($con == mysqli_connect_error())
	 echo "Error in connecting with the database";
 else{
	 	
	$name_ques = $_POST['name_ques'];
	$place_ques = $_POST['place_ques'];
	$num_ques = $_POST['num_ques'];

	$combID =1;
    $query = "select min(TempID) as m from comb_template where CombID = $combID";	
	$result = mysqli_query($con,$query);
	$row1 = mysqli_fetch_assoc($result);
	$minvalue = $row1["m"];
	
	$query = "select max(TempID) as m from comb_template where CombID = $combID";	
	$result = mysqli_query($con,$query);
	$row1 = mysqli_fetch_assoc($result);
	$maxvalue = $row1["m"];
	
	
	//----------generate template start----------
	$rand_template = rand($minvalue,$maxvalue);   //2=Name campus in Place university has Number students
 
	$sql_sel_temp = "select template from template where tempID=$rand_template";
	$results_temp = mysqli_query($con,$sql_sel_temp);
	
		while($row_temp = mysqli_fetch_assoc($results_temp))
		{
			$template = $row_temp["template"];
			$template = str_replace("Name",$name_ques,$template);
			$template = str_replace("Place",$place_ques,$template);
			$template = str_replace("Number",$num_ques,$template);
		}
	echo $template. ":".$rand_template;
 }
?>