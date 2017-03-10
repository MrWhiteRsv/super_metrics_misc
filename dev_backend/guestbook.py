#!/usr/bin/env python

# Copyright 2016 Google Inc.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

import json
import logging
import os
import urllib

from google.appengine.api import users
from google.appengine.ext import ndb

import jinja2
import webapp2

JINJA_ENVIRONMENT = jinja2.Environment(
    loader=jinja2.FileSystemLoader(os.path.dirname(__file__)),
    extensions=['jinja2.ext.autoescape'],
    autoescape=True)

class MainPage(webapp2.RequestHandler):
  def get(self):
    guestbook_name = self.request.get('guestbook_name', DEFAULT_GUESTBOOK_NAME)
    self.response.write('HHHH1 ' + guestbook_name)

class Product(webapp2.RequestHandler):
  def get(self):
    product_uuid = self.request.get('product_uuid', None)
    f = open('res.jsn', 'r')
    for wire in f:
      try:
        product_info = json.loads(wire)
      except ValueError, e:
        continue
      logging.info('product_info[\'uuid\']: ' + product_info['uuid'])
      if (product_uuid == product_info['uuid']):
        self.response.write('Description: ' + wire)
        return;

app = webapp2.WSGIApplication([
    ('/', MainPage),
    ('/product', Product),
], debug=True)
