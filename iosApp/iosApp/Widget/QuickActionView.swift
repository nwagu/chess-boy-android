//
//  CardView.swift
//  iosApp
//
//  Created by Chukwuemeka Nwagu on 27/06/2021.
//  Copyright © 2021 orgName. All rights reserved.
//

import SwiftUI

struct QuickActionView: View, Hashable {
    let text: String

    var body: some View {
        ZStack(alignment: Alignment(horizontal: .leading, vertical: .center)) {
            RoundedRectangle(cornerRadius: 8, style: .continuous)
                .strokeBorder(Color.gray, lineWidth: 1)

            VStack {
                Text(text)
                    .font(.body)
                    .foregroundColor(.black)
            }
            .padding(8)
            .multilineTextAlignment(.leading)
        }
        .frame(width: 120, height: 120, alignment: .center)
        .aspectRatio(contentMode: /*@START_MENU_TOKEN@*/.fill/*@END_MENU_TOKEN@*/)
    }
}

struct QuickActionView_Previews: PreviewProvider {
    static var previews: some View {
        QuickActionView(text: "Hello")
    }
}
