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
<div class="span3">
<#include "../includes/left-sidebar.ftl">
</div>

<div class="span9">
  <ul class="breadcrumb">
    <li>
      <a href="#">Home</a> <span class="divider">/</span>
    </li>
    <li>
      <a href="#">Audio</a> <span class="divider">/</span>
    </li>
    <li class="active">
      <a href="#">MP3 players</a>
    </li>
  </ul>


  <div class="row">
    <div class="span9">
      <h1>Ipod touch</h1>
    </div>
  </div>
  <hr>

  <div class="row">
    <div class="span3">
      <img alt="" src="/images/ipod_touch.jpg"/>


      <ul class="thumbnails">

        <li class="span1">
          <a href="#" class="thumbnail">
            <img src="/images/1_50.jpg" alt="">
          </a>
        </li>

        <li class="span1">
          <a href="#" class="thumbnail">
            <img src="/images/2_50.jpg" alt="">
          </a>
        </li>

        <li class="span1">
          <a href="#" class="thumbnail">
            <img src="/images/3_50.jpg" alt="">
          </a>
        </li>

      </ul>

    </div>

    <div class="span6">

      <div class="span6">
        <address>
          <strong>Brand:</strong> <span>Apple</span><br/>
          <strong>Product Code:</strong> <span>Product 14</span><br/>
          <strong>Reward Points:</strong> <span>0</span><br/>
          <strong>Availability:</strong> <span>Out Of Stock</span><br/>
        </address>
      </div>

      <div class="span6">
        <h2>
          <strong>Price: $587.50</strong>
          <small>Ex Tax: $500.00</small>
          <br/><br/>
        </h2>
      </div>

      <div class="span6">
        <form class="form-inline">
          <div class="span3 no_margin_left">
            <label>Qty:</label>
            <input type="text" class="span1" placeholder="1">
            <button class="btn btn-primary" type="submit">Add to cart</button>
          </div>
          <div class="span1">
            - OR -
          </div>
          <div class="span2">
            <p><a href="#">Add to Wish List</a></p>

            <p><a href="compare">Add to Compare</a></p>
          </div>
        </form>
      </div>

      <div class="span6">

        <p>
          <input name="star1" type="radio" class="star"/>
          <input name="star1" type="radio" class="star"/>
          <input name="star1" type="radio" class="star"/>
          <input name="star1" type="radio" class="star"/>
          <input name="star1" type="radio" class="star"/>&nbsp;&nbsp;

          <a href="#">0 reviews</a> | <a href="#">Write a review</a></p>
      </div>


    </div>


  </div>
  <hr>
  <div class="row">
    <div class="span9">
      <div class="tabbable">
        <ul class="nav nav-tabs">
          <li class="active"><a href="#1" data-toggle="tab">Description</a></li>
          <li><a href="#2" data-toggle="tab">Reviews</a></li>
          <li><a href="#3" data-toggle="tab">Related products</a></li>
        </ul>
        <div class="tab-content">
          <div class="tab-pane active" id="1">
            <p>Just when you thought iMac had everything, now there&apos;s even more. More powerful Intel Core 2 Duo
              processors. And more memory standard. Combine this with Mac OS X Leopard and iLife &apos;08, and it&apos;s
              more all-in-one than ever. iMac packs amazing performance into a stunningly slim space.
            </p>
          </div>
          <div class="tab-pane" id="2">
            <p>There are no reviews for this product.</p>
          </div>
          <div class="tab-pane" id="3">
            <ul class="thumbnails related_products">

              <li class="span2">
                <div class="thumbnail">
                  <a href="item"><img alt="" src="http://placehold.it/220x180"/></a>

                  <div class="caption">
                    <a href="item"><h5>iPod Touch</h5></a> Price: &#36;50.00<br/><br/>
                  </div>
                </div>
              </li>

              <li class="span2">
                <div class="thumbnail">
                  <a href="item"><img alt="" src="http://placehold.it/220x180"/></a>

                  <div class="caption">
                    <a href="item"><h5>iPod Touch</h5></a> Price: &#36;50.00<br/><br/>
                  </div>
                </div>
              </li>

              <li class="span2">
                <div class="thumbnail">
                  <a href="item"><img alt="" src="http://placehold.it/220x180"/></a>

                  <div class="caption">
                    <a href="item"><h5>iPod Touch</h5></a> Price: &#36;50.00<br/><br/>
                  </div>
                </div>
              </li>

              <li class="span2">
                <div class="thumbnail">
                  <a href="item"><img alt="" src="http://placehold.it/220x180"/></a>

                  <div class="caption">
                    <a href="item"><h5>iPod Touch</h5></a> Price: &#36;50.00<br/><br/>
                  </div>
                </div>
              </li>


            </ul>
          </div>
        </div>
      </div>

    </div>
  </div>


</div>

</div><#include "../includes/footer.ftl">

</div>
<!-- /container -->

<#include "../includes/cdn-scripts.ftl">

</body>
</html>