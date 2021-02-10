
		$(document).ready(function(){
			//alert("ok");
			$("#uomTypeError").hide();
			$("#uomModelError").hide();
			$("#descriptionError").hide();

			var uomTypeError=false;
			var uomModelError=false;
			var descriptionError = false;

			$("#uomType").change(function(){
				validate_uomType();
			})

			$("#uomModel").keyup(function(){
				$("#uomModel").val($("#uomModel").val().toUpperCase());
				validate_uomModel();
			});

			$("#description").keyup(function () {
	            validate_description();
			 });
			 
			//===================User function validation================================
			function validate_uomType(){
				//alert("ok");
				var val=$("#uomType").val();
				if (val=='') {
					$("#uomTypeError").show();
					$("#uomTypeError").html("Choose <b>Uom Type </b>");
					$("#uomTypeError").css("color","red");
					uomTypeError=false;
				} else {
					$("#uomTypeError").hide();
					uomTypeError=true;
				}
				return uomTypeError;
			}

			function validate_uomModel(){
				var val=$("#uomModel").val();
				var exp=/^[A-Z]{2,25}$/;
				if (val=='') {
					$("#uomModelError").show();
					$("#uomModelError").html("Enter <b>Uom Model </b>");
					$("#uomModelError").css("color","red");
					uomModelError=false;
				} else if(!exp.test(val)){
					$("#uomModelError").show();
					$("#uomModelError").html("Enter <b>2-25 Chars Only </b>");
					$("#uomModelError").css("color","red");
					uomModelError=false;
				}else{
						$("#uomModelError").hide();
						uomModelError=true;
					//===================Ajax call===========
				//	$.ajax({
						//alert("og");
				//		url:'validatemodel',
					//	data: {"code":val},
			//			success: function(resTxt){
			//				if (resTxt!="") {
				//				$("#uomModelError").show();
					//			$("#uomModelError").html(resTxt);
					//			$("#uomModelError").css("color","red");
					//			uomModelError=false;
					//		} else {
					//			$("#uomModelError").hide();
					//			uomModelError=true;
						//	}
						}
					});//Ajax end
				}//End if-else
				return uomModelError;
			}
			
			function validate_description() {
                 var val = $("#description").val();
                 if (val.length < 5 || val.length > 150) {
                     $("#descriptionError").show();
                     $("#descriptionError").html("Must be <b>5-150 Chars only</b>");
                     $("#descriptionError").css("color", "red");
                     descriptionError = false;
                 } else {
                     $("#descriptionError").hide();
                     descriptionError = true;
                 }
                 return descriptionError;
             }

			//===================On Submit===========================================
			$("#uomForm").submit(function(){
				validate_uomType();
				validate_uomModel();
				validate_description();

				if (uomTypeError && uomModelError  && descriptionError) {
					return true;
				} else {
					return false;
				}
			});
		});
