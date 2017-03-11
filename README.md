# super_metrics_misc

## Backend

### all products

curl http://localhost:8080/all_products

["836093010028", "14602", "14097", "14004", "046567000299", "00049000000443", "45155607", "762111083173", "45146174", "072036080196]

### product details

curl http://localhost:8080/product_details?product_uuid=762111083173

{"category": "gourmet", "location_str": "aisle 11", "uuid": "762111083173", "ingredients": "GREEN TEAS, NATURAL FLAVORS, LEMON VERBENA, SPEARMINT, LEMONGRASS, LICORICE ROOT.", "ndbno": "45110599", "nutrients": [{"unit": "kcal", "name": "Energy", "value": "0"}], "price": "$4.49", "location_px": {"px": 4175, "py": 0.2}, "description": "TAZO, GREEN TEA WITH BRIGHT MANGO & CITRUS NOTES, ICED MANGO GREEN, UPC: 762111083173", "discount_percent": 0, "images": ["tazo.png"], "name": "Tazo"}


## MQTT

### Web Client Messages

* Cart Location
  * Chanel: monitor/cartId/location
  * Example: {"px":0.16085714285714287,"py":0.12}

#### Commands

* Change BLE threshold
  * Channel: monitor/cartId/command
  * Example: {"changeThreshold":true, "mac":"34:b1:f7:d3:91:f8", "threshold":-50}

* Capture Image
  * Channel: monitor/cartId/command
  * Example: {"changeThreshold":true, "mac":"34:b1:f7:d3:91:f8", "threshold":-50}

### Cart Detector Messages

* Revolution
 * Chanel: cart/cartId/revolution
 * Example: {"start_time": 1488995981.953236, "forward_counter": 371, "backward_counter": 0, "forward_revolution": true}  
* BLE
 * Chanel: cart/cartId/ble
 * Example: cart/cartId/ble {"nearest_rssi": -36, "start_time": 1488995907, "mac": "34:b1:f7:d3:9e:2b", "nearest_time": 1488995909, "end_time": 1488995916}
