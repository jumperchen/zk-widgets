imagelabel.ImageLabel = zk.$extends(zul.LabelImageWidget, {
	_orient: 'vertical',
	_dir: 'normal',
	
	$define: {
		orient: _zkf = function () {
			this.updateDomContent_();
		},
		
		dir: _zkf
	},
	
	bind_: function () {
		this.$supers(imagelabel.ImageLabel,'bind_', arguments);
	},
	
	unbind_: function () {
		this.$supers(imagelabel.ImageLabel,'unbind_', arguments);
	},
	
	domContent_: function () {
		var label = zUtl.encodeXML(this.getLabel()),
			img = this.getImage();
		if (!img) return label;

		img = '<img src="' + img + '" align="absmiddle" />';
		var space = "vertical" == this.getOrient() ? '<br/>': ' ';
		return this.getDir() == 'reverse' ?
			label + space + img: img + space + label;
	},
	
	getZclass: function () {
		return this._zclass != null ? this._zclass: "z-imagelabel";
	}
});