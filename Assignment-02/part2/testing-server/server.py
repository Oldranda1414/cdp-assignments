from flask import Flask
from flask import render_template
from flask import abort

### Instantiating Flask app
app = Flask(__name__)

###
### Rest APIs
###

@app.route('/ping', methods = ['GET'])
def ping():
    return 'Pong'

# LEVEL 0
@app.route('/', methods = ['GET'])
def homepage():
    return render_template('homepage.html')

# LEVEL 1
@app.route('/page_one', methods = ['GET'])
def page_one():
    return render_template('page_one.html')

@app.route('/page_two', methods = ['GET'])
def page_two():
    return render_template('page_two.html')

@app.route('/page_three', methods = ['GET'])
def page_three():
    return abort(404)

# LEVEL 2
@app.route('/page_one/<number>', methods = ['GET'])
def page_one_number(number):
    return render_template('page_one_number.html', number = number)

@app.route('/page_two/<number>', methods = ['GET'])
def page_two_number(number):
    return render_template('page_two_number.html', number = number)

### Waitress run
if __name__ == '__main__':
    from waitress import serve
    port = 8080
    print('Rest API running on port ' + str(port))
    serve(app, host = '0.0.0.0', port = port)
