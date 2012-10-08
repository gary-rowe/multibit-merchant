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


      <h1> Shopping Cart</h1><br/>

      <table class="table table-bordered table-striped">
        <thead>
        <tr>
          <th>Remove</th>
          <th>Image</th>
          <th>Product Name</th>
          <th>Model</th>
          <th>Quantity</th>
          <th>Unit Price</th>
          <th>Total</th>
        </tr>
        </thead>
        <tbody>
        <tr>
          <td class=""><input type="checkbox" value="option1" id="optionsCheckbox"></td>
          <td class="muted center_text"><a href="item"><img src="/images/macbook-pro.jpg"></a></td>
          <td>MacBook Pro</td>
          <td>Product 18</td>
          <td><input type="text" placeholder="1" class="input-mini"></td>
          <td>$2,350.00</td>
          <td>$2,350.00</td>
        </tr>
        <tr>
          <td class=""><input type="checkbox" value="option1" id="optionsCheckbox"></td>
          <td class="muted center_text"><a href="item"><img src="/images/macbook-pro.jpg"></a></td>
          <td>MacBook Pro</td>
          <td>Product 18</td>
          <td><input type="text" placeholder="1" class="input-mini"></td>
          <td>$2,350.00</td>
          <td>$2,350.00</td>
        </tr>
        <tr>
          <td>&nbsp;</td>
          <td>&nbsp;</td>
          <td>&nbsp;</td>
          <td>&nbsp;</td>
          <td>&nbsp;</td>
          <td>&nbsp;</td>
          <td><strong>$4,700.00</strong></td>
        </tr>
        </tbody>
      </table>

      <form class="form-horizontal">
        <fieldset>


          <div class="accordion" id="accordion2">
            <div class="accordion-group">
              <div class="accordion-heading">

                <a class="accordion-toggle" data-toggle="collapse" data-parent="#accordion2" href="#collapseOne">
                  <h3>Apply discount code</h3>
                </a>
              </div>
              <div id="collapseOne" class="accordion-body collapse in">
                <div class="accordion-inner">
                  <div class="control-group">
                    <label for="input01" class="control-label">Discount code: </label>

                    <div class="controls">
                      <input type="text" id="input01" class="input-xlarge" placeholder="Enter your coupon here">

                      <p class="help-block">You can only use one discount code at a time</p>
                    </div>
                  </div>
                </div>
              </div>
            </div>

            <div class="accordion-group">
              <div class="accordion-heading">
                <a class="accordion-toggle" data-toggle="collapse" data-parent="#accordion2" href="#collapseTwo">
                  <h3>Use gift voucher</h3>
                </a>
              </div>
              <div id="collapseTwo" class="accordion-body collapse">
                <div class="accordion-inner">
                  <div class="control-group">
                    <label for="input01" class="control-label">Gift voucher: </label>

                    <div class="controls">
                      <input type="text" id="input01" class="input-xlarge" placeholder="Enter your gift voucher here">

                      <p class="help-block">You can use multiple gift vouchers at a time</p>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <div class="row">
            <div class="span5">
              <button class="btn btn-primary" type="submit">Update</button>
            </div>
            <div class="span2">
              <button class="btn btn-primary" type="submit">Continue shopping</button>
            </div>
            <div class="span5">
              <a href="checkout" class="btn btn-primary pull-right">Checkout</a>
            </div>
          </div>
        </fieldset>
      </form>


    <#include "../includes/footer.ftl">

    </div>
    <!-- /container -->

  <#include "../includes/cdn-scripts.ftl">


</body>
</html>