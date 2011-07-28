org.zkoss.imagelabel.Imagelabel = zk.$extends(zul.LabelImageWidget, {
	_label: '',
	_image: '',
	$define : {
		label: zkf = function(v) {
			return !v ? '' : v;
		},
		image: zkf
	},
	
	getZclass : function() {
		var zcls = this._zclass;
		return zcls ? zcls : 'z-imagelabel';
	}
});