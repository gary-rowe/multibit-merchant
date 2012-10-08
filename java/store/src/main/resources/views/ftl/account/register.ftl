<!DOCTYPE html>
<html lang="en">
<head>
<#include "../includes/head.ftl">
</head>

<body>

<div class="container">
<#include "../includes/header.ftl">
<#include "../includes/navbar.ftl">
  <div class="row">

    <div class="span12">
      <ul class="breadcrumb">
        <li><a href="#">Home</a> <span class="divider">/</span></li>
        <li><a href="#">Account</a> <span class="divider">/</span></li>
        <li class="active"><a href="#">Register</a></li>
      </ul>
    </div>

    <div class="span12">
      <h1>Create an account</h1>

      <br/>

      <form class="form-horizontal">
        <fieldset>
          <div class="span6 no_margin_left">
            <legend>Your Personal Details</legend>
            <div class="control-group">
              <label class="control-label">First Name</label>

              <div class="controls docs-input-sizes">
                <input type="text" placeholder="" class="span4">
              </div>
            </div>
            <div class="control-group">
              <label class="control-label">Last Name</label>

              <div class="controls docs-input-sizes">
                <input type="text" placeholder="" class="span4">
              </div>
            </div>
            <div class="control-group">
              <label class="control-label">Email Address</label>

              <div class="controls docs-input-sizes">
                <input type="text" placeholder="" class="span4">
              </div>
            </div>

            <div class="control-group">
              <label class="control-label">Telephone</label>

              <div class="controls docs-input-sizes">
                <input type="text" placeholder="" class="span4">
              </div>
            </div>
          </div>
          <div class="span6">
            <legend>Your Address</legend>
            <div class="control-group">
              <label class="control-label">Address 1</label>

              <div class="controls docs-input-sizes">
                <input type="text" placeholder="" class="span4">
              </div>
            </div>
            <div class="control-group">
              <label class="control-label">Address 2</label>

              <div class="controls docs-input-sizes">
                <input type="text" placeholder="" class="span4">
              </div>
            </div>
            <div class="control-group">
              <label class="control-label">City</label>

              <div class="controls docs-input-sizes">
                <input type="text" placeholder="" class="span4">
              </div>
            </div>
            <div class="control-group">
              <label class="control-label">ZIP</label>

              <div class="controls docs-input-sizes">
                <input type="text" placeholder="" class="span4">
              </div>
            </div>
            <div class="control-group">
              <label class="control-label">Country</label>

              <div class="controls docs-input-sizes">
                <input type="text" placeholder="" class="span4">
              </div>
            </div>
          </div>

          <div class="span12 no_margin_left">
            <legend>Your login</legend>
            <div class="span6 no_margin_left">
              <div class="control-group">
                <label class="control-label">Username</label>

                <div class="controls docs-input-sizes">
                  <input type="text" placeholder="" class="span4">
                </div>
              </div>
            </div>
            <div class="span6">
              <div class="control-group">
                <label class="control-label">Password</label>

                <div class="controls docs-input-sizes">
                  <input type="text" placeholder="" class="span4">
                </div>
              </div>
              <div class="control-group">
                <label class="control-label">Confirm password</label>

                <div class="controls docs-input-sizes">
                  <input type="text" placeholder="" class="span4">
                </div>
              </div>
            </div>


          </div>


          <div class=" span12 no_margin_left">
            <hr>
            <div class="span8">
              <input type="checkbox" value="option1" name="optionsCheckboxList1"> I have read and agree to the Privacy
              Policy<br/>
              <input type="checkbox" value="option2" name="optionsCheckboxList2"> Subscribe to our newsletter

            </div>
            <div class="span3">
              <button class="btn btn-primary btn-large pull-right" type="submit">Register</button>
            </div>
            <hr>
          </div>
        </fieldset>
      </form>

    </div>

    <hr/>


  </div>

<#include "../includes/footer.ftl">

</div>
<!-- /container -->

<#include "../includes/cdn-scripts.ftl">

</body>
</html>