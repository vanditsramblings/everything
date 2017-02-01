//An attempt to make a web version of web paint application

var gl; 
var program;
var vs ;
var fs;


function start() {
  var canvas = document.getElementById("glcanvas");
  gl = initWebGL(canvas);  
  if (gl) {
    gl.clearColor(0.0, 0.0, 0.0, 1.0);
    gl.enable(gl.DEPTH_TEST);
    gl.depthFunc(gl.LEQUAL);
    gl.clear(gl.COLOR_BUFFER_BIT|gl.DEPTH_BUFFER_BIT);
	compileShaders(gl);
	checkShaderLinking(gl);
	gl.useProgram(program);
	drawShapes(gl,canvas);
    
    program.uColor = gl.getUniformLocation(program, "uColor");
    gl.uniform4fv(program.uColor, [1, 1, 0.0, 1.0]);
     
    program.aVertexPosition = gl.getAttribLocation(program, "aVertexPosition");
    gl.enableVertexAttribArray(program.aVertexPosition);
    gl.vertexAttribPointer(program.aVertexPosition, itemSize, gl.FLOAT, false, 0, 0);
  }
}

function initWebGL(canvas) {
  try {
	//Init web gl 
    return canvas.getContext("webgl");
  }
  catch(e) {
    console.log(e);
  }
  if (!gl) {
    alert("Browser not supported");
    return null;
  }
}


function compileShaders(gl){
	var v = document.getElementById("vertex").firstChild.nodeValue;
    var f = document.getElementById("fragment").firstChild.nodeValue;
     
    vs = gl.createShader(gl.VERTEX_SHADER);
    gl.shaderSource(vs, v);
    gl.compileShader(vs);
     
    fs = gl.createShader(gl.FRAGMENT_SHADER);
    gl.shaderSource(fs, f);
    gl.compileShader(fs);
     
    program = gl.createProgram();
    gl.attachShader(program, vs);
    gl.attachShader(program, fs);
    gl.linkProgram(program);
}

function checkShaderLinking(gl){
	if (!gl.getShaderParameter(vs, gl.COMPILE_STATUS))
            console.log(gl.getShaderInfoLog(vs));
     
    if (!gl.getShaderParameter(fs, gl.COMPILE_STATUS))
            console.log(gl.getShaderInfoLog(fs));
     
    if (!gl.getProgramParameter(program, gl.LINK_STATUS))
            console.log(gl.getProgramInfoLog(program));

}

function drawShapes(gl,canvas) {
	var aspect = canvas.width / canvas.height;
     
    var vertices = new Float32Array([
            -0.5, 0.5*aspect, 0.5, 0.5*aspect,  0.5,-0.5*aspect,
            -0.5, 0.5*aspect, 0.5,-0.5*aspect, -0.5,-0.5*aspect
            ]);
     
    vbuffer = gl.createBuffer();
    gl.bindBuffer(gl.ARRAY_BUFFER, vbuffer);                                       
    gl.bufferData(gl.ARRAY_BUFFER, vertices, gl.STATIC_DRAW);
     
    itemSize = 2;
    numItems = vertices.length / itemSize;
	
	gl.drawArrays(gl.TRIANGLES, 0, numItems);
}
