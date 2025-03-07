
let VERSION = "4.0";

function print(msg) {
    document.getElementById("dmsg").innerText = msg;
}

function initTMap(key, ctx) {
    var script = document.createElement('script');
    script.src = `https://api.tianditu.gov.cn/api?v=${VERSION}&tk=${key}`;
    script.onload = function () {
        window.map = new T.Map('tmap', {
            projection: ctx.projection,
            minZoom: ctx.minZoom,
            maxZoom: ctx.maxZoom,
            maxBounds: ctx.maxBounds,
            center: ctx.center,
            zoom: ctx.zoom
        });
        if (ctx.enableCopyrightControl) {
            window.copyrightControl = new T.Control.Copyright();
            TMap.addControl(copyrightControl);
        }
        window.geocoder = new T.Geocoder();
        window.AndroidInterface && AndroidInterface.onTMapJavaScriptLoaded(VERSION);
    }
    document.head.appendChild(script);
}


let TMap = {
    setStyle: function (style) {
        map && map.setStyle(style);
    },
    panTo: function (lnglat) {
        map && map.panTo(lnglat);
    },
    addOverLay: function (overlay) {
        map && map.addOverLay(overlay);
    },
    removeOverLay: function (overlay) {
        map && map.removeOverLay(overlay);
    },
    clearOverLays: function () {
        map && map.clearOverLays();
    },
    addControl: function (control) {
        map && map.addControl(control);
    },
    removeControl: function (control) {
        map && map.removeControl(control);
    }
}

let TCopyright = {
    _copyrights: new Map(),
    addCopyright: function (copyright) {
        if (this._copyrights.has(copyright.id)) return;
        if (copyrightControl) {
            copyrightControl.addCopyright(copyright);
            this._copyrights[copyright.id] = copyright;
        }
    },
    removeCopyright(ident) {
        let copyright = this._copyrights[ident];
        if (copyright && copyrightControl) {
            copyrightControl.removeCopyright(this._copyrights[ident])
        }
    }
}

let TControl = {
    _zoomControl: undefined,
    _scaleControl: undefined,
    _copyright: undefined,
    _mapTypeControl: undefined,
    __overviewMapControl: undefined,
    addZoomControl: function (ctx) {
        if (this._zoomControl) return;
        let opts = {
            zoomInText: ctx.zoomInText,
            zoomOutText: ctx.zoomOutText,
            zoomInTitle: ctx.zoomInTitle,
            zoomOutTitle: ctx.zoomOutTitle,

        };
        switch (ctx.position) {
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
    removeZoomControl: function () {
        if (this._zoomControl) {
            TMap.removeControl(this._zoomControl);
            this._zoomControl = undefined;
        }
    },
    addScaleControl: function (ctx) {
        if (this._scaleControl) return;
        let opts = {
            position: Functions.stringAsPosition(ctx.position)
        };
        this._scaleControl = new T.Control.Scale(opts);
        TMap.addControl(this._scaleControl);
    },
    removeScaleControl: function () {
        if (this._scaleControl) {
            TMap.removeControl(this._scaleControl);
            this._scaleControl = undefined;
        }
    },
    addMapTypeControl: function (ctx) {
        if (this._mapTypeControl) return;
        let opts = [];
        for (it of ctx) {
            let opt = {
                title: it.title,
                icon: it.icon,
            };
            switch (it.layer) {
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
    removeMapTypeControl: function () {
        if (this._mapTypeControl) {
            TMap.removeControl(this._mapTypeControl);
            this._mapTypeControl = undefined;
        }
    },
    addOverviewMapControl: function (ctx) {
        if (this._overviewMapControl) return;
        let opts = {
            size: ctx.size,
            position: Functions.stringAsPosition(ctx.position),
            isOpen: ctx.isOpen
        };
        this._overviewMapControl = new T.Control.OverviewMap(opts);
        TMap.addControl(this._overviewMapControl);
    },
    removeOverviewMapControl: function () {
        if (this._overviewMapControl) {
            TMap.removeControl(this._overviewMapControl);
            this._overviewMapControl = undefined;
        }
    }
}

let TOverLay = {
    _overlays: new Map(),
    addMarker: function (ident, ctx) {
        if (map) {
            this.removeOverLay(ident);
            let opts = {
                "draggable": ctx.draggable,
                "title": ctx.title,
                "zIndexOffset": ctx.zIndexOffset,
                "opacity": ctx.opacity
            }
            if (ctx.icon) {
                opts.icon = new T.Icon(
                    {
                        "iconSize": new T.Point(ctx.icon.iconSize.x, ctx.icon.iconSize.y),
                        "iconUrl": ctx.icon.iconUrl,
                        "iconAnchor": new T.Point(ctx.icon.iconAnchor.x, ctx.icon.iconAnchor.y)
                    }
                );
            }
            this._overlays[ident] = new T.Marker(ctx.lngLat, opts);
            TMap.addOverLay(this._overlays[ident]);
        }
    },
    addLabel: function (ident, ctx) {
        if (map) {
            this.removeOverLay(ident);
            let label = new T.Label({
                text: ctx.text,
                offset: ctx.offset,
                position: ctx.position,
                opacity: ctx.opacity
            });
            label.setTitle(ctx.title);
            label.setFontSize(ctx.fontSize);
            label.setFontColor(ctx.fontColor);
            label.setBackgroundColor(ctx.backgroundColor);
            label.setBorderLine(ctx.borderLine);
            label.setBorderColor(ctx.borderColor);
            this._overlays[ident] = label;
            TMap.addOverLay(this._overlays[ident]);
        }
    },
    addInfoWindow: function (ident, ctx) {
        if(this._overlays[ident]) {
            let window = this._overlays[ident];
            window.setLngLat(ctx.position);
            window.setContent(ctx.content);
            window.setOffset(ctx.offset);
            window.update();
        } else {
            let window = new T.InfoWindow(ctx.content, {
                minWidth: ctx.minWidth,
                maxWidth: ctx.maxWidth,
                maxHeight: ctx.maxHeight,
                autoPan: ctx.autoPan,
                closeButton: ctx.closeButton,
                offset: ctx.offset,
                autoPanPadding: ctx.autoPanPadding,
                closeOnClick: ctx.closeOnClick
            });
            window.setLngLat(ctx.position);
            TMap.addOverLay(window);
            this._overlays[ident] = window;
        }
    },
    removeOverLay: function (ident) {
        let overlay = this._overlays[ident];
        if(overlay) {
            TMap.removeOverLay(overlay);
            this._overlays.delete(ident);
        }
    }
}

let TGeocoder = {
    getLocation: function (lnglat) {
        geocoder.getLocation(new T.LngLat(lnglat.lng, lnglat.lat), resp => {
            AndroidInterface && AndroidInterface.onLocationAddress(parseInt(resp.getStatus(), 10), JSON.stringify(lnglat), JSON.stringify(resp));
        });
    }
}

let Functions = {
    stringAsPosition: (posstr) => {
        switch (posstr) {
            case "T_ANCHOR_TOP_RIGHT":
                return T_ANCHOR_TOP_RIGHT;
            case "T_ANCHOR_BOTTOM_LEFT":
                return T_ANCHOR_BOTTOM_LEFT;
            case "T_ANCHOR_BOTTOM_RIGHT":
                return T_ANCHOR_BOTTOM_RIGHT
            default:
                return T_ANCHOR_TOP_LEFT
        }
    }
}

window.onload = function () {
    if (!window.AndroidInterface) {
        initTMap(
            "cd6a40c10b97d054b435a60eb67d23b2",
            {
                center: { lng: 116.40769, lat: 39.89945 },
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