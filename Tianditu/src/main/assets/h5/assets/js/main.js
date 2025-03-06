
let VERSION = "4.0";

function print(msg) {
    document.getElementById("dmsg").innerText = msg;
}

function initTMap(key, ctx) {
    var script = document.createElement('script');
    script.src = `https://api.tianditu.gov.cn/api?v=${VERSION}&tk=${key}`;
    script.onload = function() {
        window.map = new T.Map('tmap', {
            projection: ctx.projection,
            minZoom: ctx.minZoom,
            maxZoom: ctx.maxZoom,
            maxBounds: ctx.maxBounds,
            center: ctx.center,
            zoom: ctx.zoom
        });
        if(ctx.enableCopyrightControl) {
            window.copyrightControl = new T.Control.Copyright();
            TMap.addControl(copyrightControl);
        }
        window.geocoder = new T.Geocoder();
        window.AndroidInterface && AndroidInterface.onTMapJavaScriptLoaded(VERSION);
    }
    document.head.appendChild(script);
}


let TMap = {
    setStyle: function(style) {
        map && map.setStyle(style);
    },
    panTo: function(lnglat) {
        map && map.panTo(lnglat);
    },
    addOverLay: function(overlay) {
        map && map.addOverLay(overlay);
    },
    removeOverLay: function(overlay) {
        map && map.removeOverLay(overlay);
    },
    clearOverLays: function() {
        map && map.clearOverLays();
    },
    addControl: function(control) {
        map && map.addControl(control);
    },
    removeControl: function(control) {
        map && map.removeControl(control);
    }
}

let TCopyright = {
    _copyrights: new Map(),
    addCopyright: function(copyright) {
        if(this._copyrights.has(copyright.id)) return;
        if(copyrightControl) {
            copyrightControl.addCopyright(copyright);
            this._copyrights[copyright.id] = copyright;
        }
    },
    removeCopyright(ident) {
        let copyright = this._copyrights[ident];
        if(copyright && copyrightControl) {
            copyrightControl.removeCopyright(this._copyrights[ident])
        }
    }
}

let TControl = {
    _zoomControl: undefined,
    _scaleControl: undefined,
    _copyright: undefined,
    _mapTypeControl: undefined,
    addZoomControl: function(ctx) {
        if(this._zoomControl) return;
        let opts = {
            zoomInText: ctx.zoomInText,
            zoomOutText: ctx.zoomOutText,
            zoomInTitle: ctx.zoomInTitle,
            zoomOutTitle: ctx.zoomOutTitle,

        };
        switch(ctx.position) {
            case "T_ANCHOR_TOP_RIGHT":
                opts.position = T_ANCHOR_TOP_RIGHT;
                break
            case "T_ANCHOR_BOTTOM_LEFT":
                opts.position = T_ANCHOR_BOTTOM_LEFT;
                break
            case "T_ANCHOR_BOTTOM_RIGHT":
                opts.position = T_ANCHOR_BOTTOM_RIGHT
                break
            default:
                opts.position = T_ANCHOR_TOP_LEFT
        }
        this._zoomControl = new T.Control.Zoom(opts);
        TMap.addControl(this._zoomControl);
    },
    removeZoomControl: function() {
        if(this._zoomControl) {
            TMap.removeControl(this._zoomControl);
            this._zoomControl = undefined;
        }
    },
    addScaleControl: function(ctx) {
        if(this._scaleControl) return;
        let opts = {};
        switch(ctx.position) {
            case "T_ANCHOR_TOP_RIGHT":
                opts.position = T_ANCHOR_TOP_RIGHT;
                break
            case "T_ANCHOR_BOTTOM_LEFT":
                opts.position = T_ANCHOR_BOTTOM_LEFT;
                break
            case "T_ANCHOR_BOTTOM_RIGHT":
                opts.position = T_ANCHOR_BOTTOM_RIGHT
                break
            default:
                opts.position = T_ANCHOR_TOP_LEFT
        }
        this._scaleControl = new T.Control.Scale(opts);
        TMap.addControl(this._scaleControl);
    },
    removeScaleControl: function() {
        if(this._scaleControl) {
            TMap.removeControl(this._scaleControl);
            this._scaleControl = undefined;
        }
    },
    addMapTypeControl: function(ctx) {
        if(this._mapTypeControl) return;
        let opts = [];
        for(it of ctx) {
            let opt = {
                title: it.title,
                icon: it.icon,
            };
            switch(it.layer) {
                case "TMAP_SATELLITE_MAP":
                    opt.layer = TMAP_SATELLITE_MAP;
                    break
                case "TMAP_HYBRID_MAP":
                    opt.layer = TMAP_HYBRID_MAP;
                    break
                case "TMAP_TERRAIN_MAP":
                    opt.layer = TMAP_TERRAIN_MAP;
                    break
                case "TMAP_TERRAIN_HYBRID_MAP":
                    opt.layer = TMAP_TERRAIN_HYBRID_MAP;
                    break
                default:
                    opt.layer = TMAP_NORMAL_MAP;
                    break
            }
            opts.push(opt);
        }
        this._mapTypeControl = new T.Control.MapType(opts);
        TMap.addControl(this._mapTypeControl);
    },
    removeMapTypeControl: function() {
        if(this._mapTypeControl) {
            TMap.removeControl(this._mapTypeControl);
            this._mapTypeControl = undefined;
        }
    }
}

let TOverLay = {
    _overlays: new Map(),
    addMarker: function(ident, ctx) {
        this.removeMarker(ident);
        let opts = {
            "draggable": ctx.draggable,
            "title": ctx.title,
            "zIndexOffset": ctx.zIndexOffset,
            "opacity": ctx.opacity
        }
        if(ctx.icon) {
            opts.icon = new T.Icon(
                {
                    "iconSize": new T.Point(ctx.icon.iconSize.x, ctx.icon.iconSize.y), 
                    "iconUrl": ctx.icon.iconUrl, 
                    "iconAnchor": new T.Point(ctx.icon.iconAnchor.x, ctx.icon.iconAnchor.y)
                }
            );
        }
        if(map) {
            this._overlays[ident] = new T.Marker(ctx.lngLat, opts);
            TMap.addOverLay(this._overlays[ident]);
        }
    },
    removeMarker: function(ident) {
        this._overlays[ident] && TMap.removeOverLay(this._overlays[ident]);
    }
}

let TGeocoder = {
    getLocation: function(lnglat) {
        geocoder.getLocation(new T.LngLat(lnglat.lng, lnglat.lat), resp => {
            AndroidInterface && AndroidInterface.onLocationAddress(parseInt(resp.getStatus(), 10), JSON.stringify(lnglat), JSON.stringify(resp));
        });
    }
}

window.onload = function() {
    if(!window.AndroidInterface) {
        initTMap(
            "cd6a40c10b97d054b435a60eb67d23b2",
            {
                center: { lng: 116.40769, lat: 39.89945},
                zoom: 12,
                copyright: {
                    id: "123",
                    content: "lalal"
                }
            }
        );
        console.log("Onload Initialize called!");
    }
 }